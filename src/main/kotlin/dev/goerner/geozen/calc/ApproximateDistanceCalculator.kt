package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
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

        // Check if point is inside the exterior ring
        val insideExterior = isPointInsideRing(p, exteriorRing)

        if (insideExterior) {
            // Check if point is inside any hole
            val holeContainingPoint = interiorRings.firstOrNull { hole -> isPointInsideRing(p, hole) }

            return if (holeContainingPoint != null) {
                // Point is inside a hole - only calculate distance to that hole's boundary
                calculateMinDistanceToPositions(p, holeContainingPoint)
            } else {
                // Point is inside polygon and not in any hole
                0.0
            }
        }

        // Point is outside the polygon - calculate minimum distance to exterior ring
        return calculateMinDistanceToPositions(p, exteriorRing)
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

        // Check for segment-segment intersections
        for (i in 0 until positions1.size - 1) {
            val seg1Start = positions1[i]
            val seg1End = positions1[i + 1]

            for (j in 0 until positions2.size - 1) {
                val seg2Start = positions2[j]
                val seg2End = positions2[j + 1]

                if (GeometricUtils.doSegmentsIntersect(seg1Start, seg1End, seg2Start, seg2End)) {
                    return 0.0
                }
            }
        }

        // No intersections found, calculate minimum distance from all points in lineString1 to lineString2
        val minFromLine1 = positions1.minOf { position ->
            calculateMinDistanceToPositions(Point(position), positions2)
        }

        // Find minimum distance from all points in lineString2 to lineString1
        val minFromLine2 = positions2.minOf { position ->
            calculateMinDistanceToPositions(Point(position), positions1)
        }

        return minOf(minFromLine1, minFromLine2)
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

        // Phase 1: Check for segment intersections between LineString and any polygon ring
        for (i in 0 until lineStringPositions.size - 1) {
            val lsSegStart = lineStringPositions[i]
            val lsSegEnd = lineStringPositions[i + 1]

            // Check intersection with exterior ring
            for (j in 0 until exteriorRing.size - 1) {
                val ringSegStart = exteriorRing[j]
                val ringSegEnd = exteriorRing[j + 1]

                if (GeometricUtils.doSegmentsIntersect(lsSegStart, lsSegEnd, ringSegStart, ringSegEnd)) {
                    return 0.0
                }
            }

            // Check intersection with interior rings (holes)
            for (hole in interiorRings) {
                for (j in 0 until hole.size - 1) {
                    val holeSegStart = hole[j]
                    val holeSegEnd = hole[j + 1]

                    if (GeometricUtils.doSegmentsIntersect(lsSegStart, lsSegEnd, holeSegStart, holeSegEnd)) {
                        return 0.0
                    }
                }
            }
        }

        // Phase 2: Check containment - analyze LineString vertices
        var allVerticesInsidePolygon = true
        var anyVertexInHole = false
        var holeContainingVertex: List<Position>? = null

        for (position in lineStringPositions) {
            val point = Point(position)
            val insideExterior = isPointInsideRing(point, exteriorRing)

            if (!insideExterior) {
                allVerticesInsidePolygon = false
                break
            }

            // Check if this vertex is in any hole
            val hole = interiorRings.firstOrNull { h -> isPointInsideRing(point, h) }
            if (hole != null) {
                anyVertexInHole = true
                holeContainingVertex = hole
                allVerticesInsidePolygon = false
                break
            }
        }

        // If all vertices are inside the polygon (and not in any hole), distance is 0
        if (allVerticesInsidePolygon) {
            return 0.0
        }

        // If any vertex is in a hole, calculate distance to that hole's boundary
        if (anyVertexInHole && holeContainingVertex != null) {
            // Calculate distance from LineString vertices to hole boundary
            val minFromLineStringToHole = lineStringPositions.minOf { position ->
                calculateMinDistanceToPositions(Point(position), holeContainingVertex)
            }

            // Calculate distance from hole boundary vertices to LineString
            val minFromHoleToLineString = holeContainingVertex.minOf { position ->
                calculateMinDistanceToPositions(Point(position), lineStringPositions)
            }

            return minOf(minFromLineStringToHole, minFromHoleToLineString)
        }

        // Phase 3: No intersection or containment - calculate minimum distances
        // Calculate minimum distance from all LineString vertices to all polygon rings
        val minFromLineStringToPolygon = lineStringPositions.minOf { position ->
            val point = Point(position)
            val distToExterior = calculateMinDistanceToPositions(point, exteriorRing)
            val distToHoles = interiorRings.map { hole ->
                calculateMinDistanceToPositions(point, hole)
            }
            minOf(distToExterior, distToHoles.minOrNull() ?: Double.MAX_VALUE)
        }

        // Calculate minimum distance from all polygon ring vertices to LineString
        val allRingVertices = rings.flatten()
        val minFromPolygonToLineString = allRingVertices.minOf { position ->
            calculateMinDistanceToPositions(Point(position), lineStringPositions)
        }

        return minOf(minFromLineStringToPolygon, minFromPolygonToLineString)
    }

    /**
     * Checks if a point is inside a ring using the ray casting algorithm.
     *
     * @param p    the point
     * @param ring the ring (linear ring of positions)
     * @return true if the point is inside the ring, false otherwise
     */
    private fun isPointInsideRing(p: Point, ring: List<Position>): Boolean {
        var intersections = 0
        val px = p.longitude
        val py = p.latitude

        for (i in ring.indices) {
            val p1 = ring[i]
            val p2 = ring[(i + 1) % ring.size]

            val x1 = p1.longitude
            val y1 = p1.latitude
            val x2 = p2.longitude
            val y2 = p2.latitude

            // Check if the ray crosses this edge
            if (((y1 > py) != (y2 > py)) && (px < (x2 - x1) * (py - y1) / (y2 - y1) + x1)) {
                intersections++
            }
        }

        return intersections % 2 == 1
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
