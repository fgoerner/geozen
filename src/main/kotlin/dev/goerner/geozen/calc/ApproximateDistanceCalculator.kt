package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

object ApproximateDistanceCalculator {

    /**
     * Calculates the distance between two geographical positions using the Haversine formula.
     * 
     * 
     * 
     * This method converts the latitude and longitude of the provided `Position` objects from
     * degrees to radians, computes the differences in latitude and longitude, and calculates the
     * distance based on the Haversine formula. The Earth's radius is assumed to be 6,371,008.8 meters.
     * 
     * 
     * @param p1 the first geographical position, with latitude and longitude in degrees
     * @param p2 the second geographical position, with latitude and longitude in degrees
     * @return the computed distance between `p1` and `p2` in meters
     */
    private fun haversineDistance(p1: Position, p2: Position): Double {
        val lat1 = Math.toRadians(p1.latitude)
        val lat2 = Math.toRadians(p2.latitude)

        val lon1 = Math.toRadians(p1.longitude)
        val lon2 = Math.toRadians(p2.longitude)

        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1

        val distanceFactor: Double = 1.0 - cos(deltaLat) + cos(lat1) * cos(lat2) * (1.0 - cos(deltaLon))

        return 2.0 * 6371008.8 * asin(sqrt(distanceFactor / 2.0))
    }

    /**
     * Calculates an approximate geodesic distance between two [Point] instances.
     * 
     * 
     * This method delegates to haversineDistance by using the
     * underlying [Position] coordinates of the provided points. It is intended for
     * scenarios where a fast, reasonably accurate distance calculation is sufficient.
     * 
     * @param p1 the first point, providing latitude and longitude coordinates
     * @param p2 the second point, providing latitude and longitude coordinates
     * @return the approximate distance between `p1` and `p2` in meters
     */
    fun calculate(p1: Point, p2: Point): Double {
        return haversineDistance(p1.coordinates, p2.coordinates)
    }

    /**
     * Calculates an approximate distance between a Point and a LineString.
     * 
     * 
     * This method iterates through the segments of the LineString, projects the point onto
     * the segment using a planar approximation (Equirectangular projection), and calculates
     * the Haversine distance to the projected point. This is faster but less accurate than
     * the precise method, especially over large distances or near poles.
     * 
     * @param p          the point
     * @param lineString the line string
     * @return the approximate distance in meters
     */
    fun calculate(p: Point, lineString: LineString): Double {
        return calculateMinDistanceToPositions(p, lineString.coordinates)
    }

    /**
     * Calculates an approximate distance between a Point and a Polygon.
     *
     *
     * This method first checks if the point is inside the polygon using a ray casting algorithm.
     * If the point is inside the polygon (and not inside any holes), the distance is 0.
     * If the point is outside, it calculates the minimum distance to all rings (exterior and holes)
     * of the polygon using the same approach as point-to-linestring distance calculation.
     *
     * @param p       the point
     * @param polygon the polygon
     * @return the approximate distance in meters
     */
    fun calculate(p: Point, polygon: Polygon): Double {
        val rings = polygon.coordinates
        val exteriorRing = rings[0]
        val interiorRings = rings.drop(1)

        // Use shared helper for containment analysis
        val containmentResult = PointToPolygonDistanceHelper.analyzePointPolygonContainment(
            p.longitude,
            p.latitude,
            exteriorRing,
            interiorRings
        )

        return when (containmentResult) {
            is PointToPolygonDistanceHelper.ContainmentResult.InsidePolygon -> 0.0
            is PointToPolygonDistanceHelper.ContainmentResult.InsideHole -> {
                calculateMinDistanceToPositions(p, containmentResult.holeRing)
            }

            is PointToPolygonDistanceHelper.ContainmentResult.OutsidePolygon -> {
                calculateMinDistanceToPositions(p, exteriorRing)
            }
        }
    }

    /**
     * Calculates an approximate distance between a Point and a MultiPoint.
     *
     *
     * This method iterates through all points in the MultiPoint and calculates the Haversine
     * distance to each, returning the minimum distance found. This is faster but less accurate
     * than the precise method, especially over large distances or near poles.
     *
     * @param point      the point
     * @param multiPoint the multi-point geometry
     * @return the approximate minimum distance in meters
     */
    fun calculate(point: Point, multiPoint: MultiPoint): Double {
        return multiPoint.coordinates.minOf { haversineDistance(point.coordinates, it) }
    }

    /**
     * Calculates an approximate distance between a Point and a MultiLineString.
     *
     *
     * This method iterates through all LineStrings in the MultiLineString, calculates the
     * distance from the point to each LineString using the same approach as point-to-linestring
     * distance calculation, and returns the minimum distance found. This is faster but less
     * accurate than the precise method, especially over large distances or near poles.
     *
     * @param point           the point
     * @param multiLineString the multi-line string geometry
     * @return the approximate minimum distance in meters
     */
    fun calculate(point: Point, multiLineString: MultiLineString): Double {
        return multiLineString.coordinates.minOf { calculate(point, LineString(it)) }
    }

    /**
     * Calculates an approximate distance between a Point and a MultiPolygon.
     *
     *
     * This method iterates through all Polygons in the MultiPolygon, calculates the distance
     * from the point to each Polygon using the same approach as point-to-polygon distance
     * calculation, and returns the minimum distance found. This is faster but less accurate
     * than the precise method, especially over large distances or near poles.
     *
     * @param point        the point
     * @param multiPolygon the multi-polygon geometry
     * @return the approximate minimum distance in meters
     */
    fun calculate(point: Point, multiPolygon: MultiPolygon): Double {
        return multiPolygon.coordinates.minOf { calculate(point, Polygon(it)) }
    }

    /**
     * Calculates an approximate distance between two LineString geometries.
     *
     *
     * This method will find the minimum distance between any two segments across both
     * LineStrings using the Haversine formula and equirectangular projection approximation.
     * This is faster but less accurate than the precise method, especially over large distances
     * or near poles.
     *
     * @param lineString1 the first line string
     * @param lineString2 the second line string
     * @return the approximate minimum distance in meters
     */
    fun calculate(lineString1: LineString, lineString2: LineString): Double {
        val positions1 = lineString1.coordinates
        val positions2 = lineString2.coordinates

        // Check for segment intersections using shared helper
        if (LineStringToLineStringDistanceHelper.doLineStringsIntersect(positions1, positions2)) {
            return 0.0
        }

        // No intersections found - calculate bidirectional distance
        return LineStringToLineStringDistanceHelper.calculateBidirectionalDistance(
            positions1,
            positions2,
            ::calculateMinDistanceToPositions
        )
    }

    /**
     * Calculates an approximate distance between a LineString and a Polygon.
     *
     *
     * This method will check if any part of the LineString intersects or is contained within
     * the Polygon (accounting for holes). If not, it calculates the minimum distance between
     * the LineString and the Polygon's rings using the Haversine formula and equirectangular
     * projection approximation.
     *
     * @param lineString the line string
     * @param polygon    the polygon
     * @return the approximate minimum distance in meters
     */
    fun calculate(lineString: LineString, polygon: Polygon): Double {
        val lineStringPositions = lineString.coordinates
        val rings = polygon.coordinates
        val exteriorRing = rings[0]
        val interiorRings = rings.drop(1)

        // Phase 1 & 2: Use shared helper for intersection detection and containment analysis
        val analysisResult = LineStringToPolygonDistanceHelper.analyzeLineStringPolygonRelationship(
            lineStringPositions,
            exteriorRing,
            interiorRings
        )

        return when (analysisResult) {
            is LineStringToPolygonDistanceHelper.AnalysisResult.Intersection -> 0.0
            is LineStringToPolygonDistanceHelper.AnalysisResult.FullyContained -> 0.0
            is LineStringToPolygonDistanceHelper.AnalysisResult.InHole -> {
                LineStringToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    lineStringPositions,
                    analysisResult.holeContainingVertex,
                    ::calculateMinDistanceToPositions
                )
            }

            is LineStringToPolygonDistanceHelper.AnalysisResult.NoIntersectionOrContainment -> {
                // Phase 3: Calculate minimum distances using approximate algorithm
                LineStringToPolygonDistanceHelper.calculateDistanceForGeneralCase(
                    lineStringPositions,
                    rings,
                    exteriorRing,
                    interiorRings,
                    ::calculateMinDistanceToPositions
                )
            }
        }
    }

    /**
     * Calculates an approximate distance between two Polygon geometries.
     *
     * This method finds the minimum distance between two Polygons using a three-phase approach:
     *
     * **Phase 1: Intersection Detection**
     * - Checks all ring pairs for intersections using a planar approximation
     * - Returns 0.0 immediately if any intersection is found
     *
     * **Phase 2: Containment Analysis**
     * - Checks if polygon1 is fully contained within polygon2 (not in a hole) → distance is 0.0
     * - Checks if polygon2 is fully contained within polygon1 (not in a hole) → distance is 0.0
     * - Checks if either polygon has vertices inside a hole of the other → calculate distance to hole boundary
     *
     * **Phase 3: Distance Calculation** (if no intersections or containment)
     * - Calculates bidirectional distances between all rings of both polygons
     * - Uses Haversine formula and equirectangular projection approximation
     *
     * **Performance:**
     * - Time Complexity: O(n × m) where n and m are the total number of vertices across all rings
     * - Faster but less accurate than the precise method, especially over large distances or near poles
     *
     * @param polygon1 the first polygon
     * @param polygon2 the second polygon
     * @return the approximate minimum distance in meters
     */
    fun calculate(polygon1: Polygon, polygon2: Polygon): Double {
        val rings1 = polygon1.coordinates
        val exteriorRing1 = rings1[0]
        val interiorRings1 = rings1.drop(1)

        val rings2 = polygon2.coordinates
        val exteriorRing2 = rings2[0]
        val interiorRings2 = rings2.drop(1)

        // Phase 1 & 2: Use shared helper for intersection detection and containment analysis
        val analysisResult = PolygonToPolygonDistanceHelper.analyzePolygonPolygonRelationship(
            rings1,
            rings2,
            exteriorRing1,
            interiorRings1,
            exteriorRing2,
            interiorRings2
        )

        return when (analysisResult) {
            is PolygonToPolygonDistanceHelper.AnalysisResult.Intersection -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon1ContainedInPolygon2 -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon2ContainedInPolygon1 -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon1InHoleOfPolygon2 -> {
                PolygonToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    rings1,
                    analysisResult.holeRing,
                    ::calculateMinDistanceToPositions
                )
            }

            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon2InHoleOfPolygon1 -> {
                PolygonToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    rings2,
                    analysisResult.holeRing,
                    ::calculateMinDistanceToPositions
                )
            }

            is PolygonToPolygonDistanceHelper.AnalysisResult.NoIntersectionOrContainment -> {
                // Phase 3: Calculate minimum distances using approximate algorithm
                PolygonToPolygonDistanceHelper.calculateDistanceForGeneralCase(
                    rings1,
                    rings2,
                    ::calculateMinDistanceToPositions
                )
            }
        }
    }

    /**
     * Calculates the minimum distance from a point to a sequence of positions.
     *
     * This method iterates through consecutive pairs of positions, treating them as line segments,
     * and finds the closest point on each segment to the given point using an equirectangular
     * projection approximation. The minimum distance across all segments is returned.
     *
     * @param p         the point
     * @param positions the sequence of positions (linestring or ring)
     * @return the minimum distance in meters
     */
    private fun calculateMinDistanceToPositions(p: Point, positions: List<Position>): Double {
        if (positions.size == 1) {
            return haversineDistance(p.coordinates, positions[0])
        }

        return positions.zipWithNext { p1, p2 ->
            val projectionFactor = getSegmentProjectionFactor(p, p1, p2)

            val closestLat: Double
            val closestLon: Double

            if (projectionFactor < 0) {
                closestLat = p1.latitude
                closestLon = p1.longitude
            } else if (projectionFactor > 1) {
                closestLat = p2.latitude
                closestLon = p2.longitude
            } else {
                closestLat = p1.latitude + projectionFactor * (p2.latitude - p1.latitude)
                closestLon = p1.longitude + projectionFactor * (p2.longitude - p1.longitude)
            }

            haversineDistance(p.coordinates, Position(closestLon, closestLat))
        }.min()
    }

    /**
     * Calculates the projection factor for projecting a point onto a line segment.
     *
     * The projection factor indicates where on the segment (p1 to p2) the perpendicular
     * from point p intersects. A value of 0 means the closest point is p1, a value of 1
     * means the closest point is p2, and values between 0 and 1 indicate a point along
     * the segment. Values outside [0,1] indicate the closest point is beyond the segment ends.
     *
     * @param p  the point to project
     * @param p1 the start of the segment
     * @param p2 the end of the segment
     * @return the projection factor
     */
    private fun getSegmentProjectionFactor(p: Point, p1: Position, p2: Position): Double {
        val avgLatRad = Math.toRadians((p1.latitude + p.latitude) / 2.0)
        val x: Double = (p.longitude - p1.longitude) * cos(avgLatRad)
        val y = p.latitude - p1.latitude

        val dx: Double = (p2.longitude - p1.longitude) * cos(avgLatRad)
        val dy = p2.latitude - p1.latitude

        val dot = x * dx + y * dy
        val lenSq = dx * dx + dy * dy

        return if (lenSq != 0.0) dot / lenSq else -1.0
    }
}
