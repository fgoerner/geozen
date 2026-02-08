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

        // Phase 1: Check for segment-segment intersections
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

        // Phase 2: No intersections found - calculate minimum distances
        // Calculate minimum distance from all points in lineString1 to lineString2
        val minFromLine1 = positions1.minOf { position ->
            calculateMinDistanceToPositions(Point(position), positions2)
        }

        // Calculate minimum distance from all points in lineString2 to lineString1
        val minFromLine2 = positions2.minOf { position ->
            calculateMinDistanceToPositions(Point(position), positions1)
        }

        return minOf(minFromLine1, minFromLine2)
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
            return lineStringPositions.minOf { position ->
                calculateMinDistanceToPositions(Point(position), holeContainingVertex)
            }
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
