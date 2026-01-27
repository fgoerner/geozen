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
    fun haversineDistance(p1: Position, p2: Position): Double {
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
     * This method delegates to [haversineDistance] by using the
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
        require(rings.isNotEmpty()) { "Polygon must contain at least one ring." }

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
     * Checks if a point is inside a ring using the ray casting algorithm.
     *
     * @param p    the point
     * @param ring the ring (linear ring of positions)
     * @return true if the point is inside the ring, false otherwise
     */
    private fun isPointInsideRing(p: Point, ring: List<Position>): Boolean {
        if (ring.size < 3) return false

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
        require(positions.isNotEmpty()) { "Position list must contain at least one position." }
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
