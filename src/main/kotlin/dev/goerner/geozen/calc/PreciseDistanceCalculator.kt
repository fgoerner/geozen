package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
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
     * Calculates a precise distance between two LineString geometries.
     *
     *
     * This method will find the minimum distance between any two segments across both
     * LineStrings using Karney's algorithm from the GeographicLib library. This provides
     * highly accurate geodesic distance calculations suitable for all distances and locations
     * on Earth.
     *
     * @param lineString1 the first line string
     * @param lineString2 the second line string
     * @return the precise minimum distance in meters
     */
    fun calculate(lineString1: LineString, lineString2: LineString): Double {
        TODO()
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
        TODO()
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
