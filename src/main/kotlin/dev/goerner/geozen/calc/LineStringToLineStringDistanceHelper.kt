package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point

/**
 * Internal helper class for LineString-to-LineString distance calculations.
 *
 * This class encapsulates the shared intersection detection logic that is common to both
 * ApproximateDistanceCalculator and PreciseDistanceCalculator. Only the distance
 * calculation differs between the two calculators.
 */
internal object LineStringToLineStringDistanceHelper {

    /**
     * Checks if two LineStrings have any intersecting segments.
     *
     * @param positions1 vertices of the first LineString
     * @param positions2 vertices of the second LineString
     * @return true if any segments intersect, false otherwise
     */
    fun doLineStringsIntersect(
        positions1: List<Position>,
        positions2: List<Position>
    ): Boolean {
        // Check for segment-segment intersections
        for (i in 0 until positions1.size - 1) {
            val seg1Start = positions1[i]
            val seg1End = positions1[i + 1]

            for (j in 0 until positions2.size - 1) {
                val seg2Start = positions2[j]
                val seg2End = positions2[j + 1]

                if (GeometricUtils.doSegmentsIntersect(seg1Start, seg1End, seg2Start, seg2End)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Calculates bidirectional distance between two LineStrings.
     *
     * This helper calculates distances in both directions (LineString1 → LineString2 and
     * LineString2 → LineString1) and returns the minimum.
     *
     * @param positions1 vertices of the first LineString
     * @param positions2 vertices of the second LineString
     * @param calculateMinDistanceToPositions function to calculate minimum distance from a point to positions
     * @return the minimum distance
     */
    fun calculateBidirectionalDistance(
        positions1: List<Position>,
        positions2: List<Position>,
        calculateMinDistanceToPositions: (Point, List<Position>) -> Double
    ): Double {
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
}

