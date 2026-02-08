package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import net.sf.geographiclib.Geodesic
import net.sf.geographiclib.GeodesicMask
import kotlin.math.abs

object PreciseDistanceCalculator {

    /**
     * Calculates the distance between two geographical positions using Karney's algorithm.
     * 
     * 
     * This method utilizes the GeographicLib library by calling
     * `Geodesic.WGS84.Inverse` with the latitude and longitude of the provided positions,
     * employing the `GeodesicMask.DISTANCE` mask. The resulting `GeodesicData` object's
     * `s12` field contains the computed distance in meters.
     * 
     * @param p1 the first geographical position with latitude and longitude coordinates
     * @param p2 the second geographical position with latitude and longitude coordinates
     * @return the geodesic distance in meters between `p1` and `p2`
     */
    private fun karneyDistance(p1: Position, p2: Position): Double {
        val geodesicData = Geodesic.WGS84.Inverse(
            p1.latitude,
            p1.longitude,
            p2.latitude,
            p2.longitude,
            GeodesicMask.DISTANCE
        )
        return geodesicData.s12
    }

    /**
     * Calculates a precise geodesic distance between two [Point] instances.
     * 
     * 
     * This method delegates to karneyDistance by using the
     * underlying [Position] coordinates of the provided points. It is intended for
     * scenarios where high accuracy in distance calculation is required.
     * 
     * @param p1 the first point, providing latitude and longitude coordinates
     * @param p2 the second point, providing latitude and longitude coordinates
     * @return the precise distance between `p1` and `p2` in meters
     */
    fun calculate(p1: Point, p2: Point): Double {
        return karneyDistance(p1.coordinates, p2.coordinates)
    }

    /**
     * Calculates a precise distance between a Point and a LineString.
     * 
     * 
     * This method iterates through the segments and uses the GeographicLib Karney algorithm
     * to determine distances. It handles the geodesic nature of the segments by checking
     * azimuths to determine if the closest point lies within the segment or at an endpoint.
     * If within the segment, it projects the point onto the segment and calculates the
     * precise distance to the projected point.
     *
     * @param p          the point
     * @param lineString the line string
     * @return the precise distance in meters
     */
    fun calculate(p: Point, lineString: LineString): Double {
        return calculateMinDistanceToPositions(p, lineString.coordinates)
    }

    /**
     * Calculates a precise distance between a Point and a Polygon.
     *
     *
     * This method first checks if the point is inside the polygon using a ray casting algorithm.
     * If the point is inside the polygon (and not inside any holes), the distance is 0.
     * If the point is outside, it calculates the minimum distance to all rings (exterior and holes)
     * of the polygon using the same precise geodesic approach as point-to-linestring distance calculation.
     *
     * @param p       the point
     * @param polygon the polygon
     * @return the precise distance in meters
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
     * Calculates a precise distance between a Point and a MultiPoint.
     *
     * This method iterates through all points in the MultiPoint, calculates the distance to each using
     * the precise Karney algorithm, and returns the minimum distance found.
     *
     * @param point the point
     * @param multiPoint the multi-point geometry
     * @return the minimum distance in meters from the point to any point in the multi-point
     */
    fun calculate(point: Point, multiPoint: MultiPoint): Double {
        return multiPoint.coordinates.minOf { karneyDistance(point.coordinates, it) }
    }

    /**
     * Calculates a precise distance between a Point and a MultiLineString.
     *
     * This method iterates through all LineStrings in the MultiLineString, calculates the distance to each using
     * the precise point-to-linestring distance calculation, and returns the minimum distance found.
     *
     * @param point the point
     * @param multiLineString the multi-line string geometry
     * @return the minimum distance in meters from the point to any line string in the multi-line string
     */
    fun calculate(point: Point, multiLineString: MultiLineString): Double {
        return multiLineString.coordinates.minOf { calculate(point, LineString(it)) }
    }

    /**
     * Calculates a precise distance between a Point and a MultiPolygon.
     *
     * This method iterates through all Polygons in the MultiPolygon, calculates the distance to each using
     * the precise point-to-polygon distance calculation, and returns the minimum distance found.
     *
     * @param point the point
     * @param multiPolygon the multi-polygon geometry
     * @return the minimum distance in meters from the point to any polygon in the multi-polygon
     */
    fun calculate(point: Point, multiPolygon: MultiPolygon): Double {
        return multiPolygon.coordinates.minOf { calculate(point, Polygon(it)) }
    }

    /**
     * Calculates a precise distance between two LineString geometries.
     *
     * This method finds the minimum geodesic distance between two LineStrings using a two-phase approach:
     *
     * **Phase 1: Intersection Detection**
     * - Checks all segment pairs for intersections using a planar approximation
     * - Returns 0.0 immediately if any intersection is found
     * - The planar approximation is acceptable because segments close enough to intersect
     *   make the spheroid surface nearly planar at that scale
     *
     * **Phase 2: Distance Calculation** (if no intersections)
     * - Calculates the minimum distance from all points in LineString1 to LineString2
     * - Calculates the minimum distance from all points in LineString2 to LineString1
     * - Returns the overall minimum distance
     * - Uses Karney's algorithm for all distance calculations, providing high geodesic accuracy
     *
     * **Performance:**
     * - Time Complexity: O(n × m) where n and m are the number of segments
     * - Suitable for typical GIS use cases with LineStrings containing < 100 segments
     *
     * **Note on Antimeridian:**
     * - This implementation does not handle segments crossing the ±180° longitude line
     * - For such cases, consider normalizing coordinates or subdividing segments
     *
     * @param lineString1 the first line string
     * @param lineString2 the second line string
     * @return the precise minimum distance in meters
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
     * Calculates a precise distance between a LineString and a Polygon.
     *
     *
     * This method will check if any part of the LineString intersects or is contained within
     * the Polygon (accounting for holes). If not, it calculates the minimum distance between
     * the LineString and the Polygon's rings using Karney's algorithm from the GeographicLib
     * library.
     *
     * @param lineString the line string
     * @param polygon    the polygon
     * @return the precise minimum distance in meters
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
                // Phase 3: Calculate minimum distances using precise algorithm
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
     * Calculates a precise distance between two Polygon geometries.
     *
     * This method finds the minimum geodesic distance between two Polygons using a three-phase approach:
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
     * - Uses Karney's algorithm for all distance calculations, providing high geodesic accuracy
     *
     * **Performance:**
     * - Time Complexity: O(n × m) where n and m are the total number of vertices across all rings
     * - Suitable for typical GIS use cases with polygons containing < 100 vertices per ring
     *
     * @param polygon1 the first polygon
     * @param polygon2 the second polygon
     * @return the precise minimum distance in meters
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
                // Phase 3: Calculate minimum distances using precise algorithm
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
     * and finds the closest point on each segment to the given point using the precise geodesic
     * algorithm. The minimum distance across all segments is returned.
     *
     * @param p         the point
     * @param positions the sequence of positions (linestring or ring)
     * @return the minimum distance in meters
     */
    private fun calculateMinDistanceToPositions(p: Point, positions: List<Position>): Double {
        val distances = positions.zipWithNext { p1, p2 ->
            val gAP = Geodesic.WGS84.Inverse(
                p1.latitude,
                p1.longitude,
                p.latitude,
                p.longitude,
                GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
            )
            val gAB = Geodesic.WGS84.Inverse(
                p1.latitude,
                p1.longitude,
                p2.latitude,
                p2.longitude,
                GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
            )

            var azDiffA = abs(gAP.azi1 - gAB.azi1)
            if (azDiffA > 180) azDiffA = 360 - azDiffA

            val dist = if (azDiffA > 90) {
                // Closest is p1
                gAP.s12
            } else {
                val gBP = Geodesic.WGS84.Inverse(
                    p2.latitude,
                    p2.longitude,
                    p.latitude,
                    p.longitude,
                    GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
                )
                val gBA = Geodesic.WGS84.Inverse(
                    p2.latitude,
                    p2.longitude,
                    p1.latitude,
                    p1.longitude,
                    GeodesicMask.AZIMUTH
                )

                var azDiffB = abs(gBP.azi1 - gBA.azi1)
                if (azDiffB > 180) azDiffB = 360 - azDiffB

                if (azDiffB > 90) {
                    // Closest is p2
                    gBP.s12
                } else {
                    // Closest is on the segment. Project the point onto the segment
                    // and calculate distance to the projected point
                    val distanceFromA = gAP.s12

                    // Project the point onto the segment using law of cosines
                    val projectionDistance = distanceFromA * kotlin.math.cos(Math.toRadians(azDiffA))

                    // Find the point on the segment at this distance from A
                    val projectedPoint = Geodesic.WGS84.Direct(
                        p1.latitude,
                        p1.longitude,
                        gAB.azi1,
                        projectionDistance
                    )

                    // Calculate distance from P to the projected point
                    karneyDistance(p.coordinates, Position(projectedPoint.lon2, projectedPoint.lat2))
                }
            }
            dist
        }
        return distances.min()
    }
}
