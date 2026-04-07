package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point
import net.sf.geographiclib.Geodesic
import net.sf.geographiclib.GeodesicMask

/**
 * A [DistanceCalculator] that uses Karney's geodesic algorithm (via GeographicLib) for precise
 * distance calculations. More accurate than [ApproximateDistanceCalculator] at the cost of
 * additional computation.
 */
object PreciseDistanceCalculator : AbstractDistanceCalculator() {

    /**
     * Calculates the distance between two geographical positions using Karney's algorithm.
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
    override fun calculate(p1: Position, p2: Position): Double {
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
     * Calculates the minimum distance from a point to a sequence of positions.
     *
     * This method iterates through consecutive pairs of positions, treating them as line segments,
     * and finds the closest point on each segment to the given point using the precise geodesic
     * algorithm. It handles the geodesic nature of the segments by checking azimuths to determine
     * if the closest point lies within the segment or at an endpoint. The minimum distance across
     * all segments is returned.
     *
     * @param p         the point
     * @param positions the sequence of positions (linestring or ring)
     * @return the minimum distance in meters
     */
    override fun calculateMinDistanceToPositions(p: Point, positions: List<Position>): Double {
        if (positions.size == 1) {
            return calculate(p.coordinates, positions[0])
        }

        val distances = positions.zipWithNext { p1, p2 ->
            val gAP = Geodesic.WGS84.Inverse(
                p1.latitude, p1.longitude, p.latitude, p.longitude,
                GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
            )
            val gAB = Geodesic.WGS84.Inverse(
                p1.latitude, p1.longitude, p2.latitude, p2.longitude,
                GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
            )

            val azDiffA = normalizeAzimuthDiff(gAP.azi1 - gAB.azi1)

            val dist = if (azDiffA > 90) {
                // Closest is p1
                gAP.s12
            } else {
                val gBP = Geodesic.WGS84.Inverse(
                    p2.latitude, p2.longitude, p.latitude, p.longitude,
                    GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH
                )
                val gBA = Geodesic.WGS84.Inverse(
                    p2.latitude, p2.longitude, p1.latitude, p1.longitude,
                    GeodesicMask.AZIMUTH
                )

                val azDiffB = normalizeAzimuthDiff(gBP.azi1 - gBA.azi1)

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
                        p1.latitude, p1.longitude, gAB.azi1, projectionDistance
                    )

                    // Calculate distance from P to the projected point
                    calculate(p.coordinates, Position(projectedPoint.lon2, projectedPoint.lat2))
                }
            }
            dist
        }
        return distances.min()
    }

    /**
     * Normalises a raw azimuth difference to the range [0, 180] degrees.
     *
     * GeographicLib returns azimuths in [-180, 180]. The raw difference of two such values
     * therefore lies in [-360, 360]. The normalisation proceeds in two steps:
     *
     * 1. `kotlin.math.abs(diff)` → [0, 360]
     * 2. If the result exceeds 180, subtract it from 360 to fold it back into [0, 180].
     *    (e.g. a 200° difference equals the same angular separation as 160°, just measured
     *     the other way around the circle.)
     *
     * The resulting value represents the smallest angular separation between the two azimuths,
     * which is then compared against 90° to decide whether the closest point on a geodesic
     * segment lies at an endpoint or in the interior.
     *
     * @param diff the raw difference `azi1 - azi2`, in degrees, with no assumed range
     * @return the absolute angular difference in [0, 180] degrees
     */
    private fun normalizeAzimuthDiff(diff: Double): Double {
        val abs = kotlin.math.abs(diff)
        return if (abs > 180) 360 - abs else abs
    }
}
