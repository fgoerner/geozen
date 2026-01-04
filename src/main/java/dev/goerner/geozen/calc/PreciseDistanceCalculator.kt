package dev.goerner.geozen.calc

import dev.goerner.geozen.calc.PreciseDistanceCalculator.karneyDistance
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
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
    fun karneyDistance(p1: Position, p2: Position): Double {
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
     * This method delegates to [karneyDistance] by using the
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
     * If within the segment, it calculates the cross-track distance on the ellipsoid.
     * 
     * @param p          the point
     * @param lineString the line string
     * @return the precise distance in meters
     */
    fun calculate(p: Point, lineString: LineString): Double {
        var minDistance = Double.MAX_VALUE
        val positions = lineString.coordinates

        require(positions != null && positions.isNotEmpty()) { "LineString must contain at least one position." }
        if (positions.size == 1) {
            return karneyDistance(p.coordinates, positions[0])
        }

        for (i in 0..<positions.size - 1) {
            val p1 = positions[i]
            val p2 = positions[i + 1]

            val gAP = Geodesic.WGS84.Inverse(
                p1.latitude,
                p1.longitude,
                p.latitude,
                p.longitude,
                GeodesicMask.DISTANCE or GeodesicMask.AZIMUTH or GeodesicMask.REDUCEDLENGTH
            )
            val gAB = Geodesic.WGS84.Inverse(
                p1.latitude,
                p1.longitude,
                p2.latitude,
                p2.longitude,
                GeodesicMask.AZIMUTH
            )

            var azDiffA = abs(gAP.azi1 - gAB.azi1)
            if (azDiffA > 180) azDiffA = 360 - azDiffA

            val dist: Double
            if (azDiffA > 90) {
                // Closest is p1
                dist = gAP.s12
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

                dist = if (azDiffB > 90) {
                    // Closest is p2
                    gBP.s12
                } else {
                    // Closest is on the segment. Use reduced length method for ellipsoidal cross-track distance.
                    // The reduced length m12 relates azimuth perturbations to perpendicular displacement.
                    abs(Math.toRadians(azDiffA) * gAP.m12)
                }
            }

            if (dist < minDistance) {
                minDistance = dist
            }
        }
        return minDistance
    }
}
