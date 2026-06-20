package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sqrt

/**
 * A [DistanceCalculator] that uses the Haversine formula for fast, approximate geodesic distance
 * calculations. Less accurate than [PreciseDistanceCalculator] over long distances or near the
 * poles, but significantly faster.
 */
object ApproximateDistanceCalculator : AbstractDistanceCalculator() {

    /**
     * Calculates the distance between two geographical positions using the Haversine formula.
     *
     * This method converts the latitude and longitude of the provided `Position` objects from
     * degrees to radians, computes the differences in latitude and longitude, and calculates the
     * distance based on the Haversine formula. The Earth's radius is assumed to be 6,371,008.8
     * meters.
     *
     * @param p1 the first geographical position, with latitude and longitude in degrees
     * @param p2 the second geographical position, with latitude and longitude in degrees
     * @return the computed distance between `p1` and `p2` in meters
     */
    override fun calculate(p1: Position, p2: Position): Double {
        val lat1 = Math.toRadians(p1.latitude)
        val lat2 = Math.toRadians(p2.latitude)

        val lon1 = Math.toRadians(p1.longitude)
        val lon2 = Math.toRadians(p2.longitude)

        val deltaLat = lat2 - lat1
        val deltaLon = lon2 - lon1

        val distanceFactor: Double =
            1.0 - cos(deltaLat) + cos(lat1) * cos(lat2) * (1.0 - cos(deltaLon))

        return 2.0 * 6371008.8 * asin(sqrt(distanceFactor / 2.0))
    }

    /**
     * Calculates the minimum distance from a point to a sequence of positions.
     *
     * This method iterates through consecutive pairs of positions, treating them as line segments,
     * and finds the closest point on each segment to the given point using an equirectangular
     * projection approximation. The minimum distance across all segments is returned.
     *
     * @param p the point
     * @param positions the sequence of positions (linestring or ring)
     * @return the minimum distance in meters
     */
    override fun calculateMinDistanceToPositions(p: Point, positions: List<Position>): Double {
        if (positions.size == 1) {
            return calculate(p.coordinates, positions[0])
        }

        return positions
            .zipWithNext { p1, p2 ->
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

                calculate(p.coordinates, Position(closestLon, closestLat))
            }
            .min()
    }

    /**
     * Calculates the projection factor for projecting a point onto a line segment.
     *
     * The projection factor indicates where on the segment (p1 to p2) the perpendicular from point
     * p intersects. A value of 0 means the closest point is p1, a value of 1 means the closest
     * point is p2, and values between 0 and 1 indicate a point along the segment. Values outside
     * [0,1] indicate the closest point is beyond the segment ends.
     *
     * @param p the point to project
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
