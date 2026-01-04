package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
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
        var minDistance = Double.MAX_VALUE
        val positions = lineString.coordinates

        require(positions != null && positions.isNotEmpty()) { "LineString must contain at least one position." }
        if (positions.size == 1) {
            return haversineDistance(p.coordinates, positions[0])
        }

        for (i in 0..<positions.size - 1) {
            val p1 = positions[i]
            val p2 = positions[i + 1]

            // Equirectangular projection approximation for the segment
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

            // Use Haversine for the actual distance measurement to the closest point
            val dist = haversineDistance(p.coordinates, Position(closestLon, closestLat))
            if (dist < minDistance) {
                minDistance = dist
            }
        }
        return minDistance
    }

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
