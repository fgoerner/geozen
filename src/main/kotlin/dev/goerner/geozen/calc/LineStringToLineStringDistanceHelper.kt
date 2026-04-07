package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point

/**
 * Internal helper for LineString-to-LineString distance calculations.
 *
 * Intersection detection is handled by [GeometricUtils.doPolylineSegmentsIntersect].
 * This object provides only the bidirectional distance helper that is common to both
 * [ApproximateDistanceCalculator] and [PreciseDistanceCalculator].
 */
internal object LineStringToLineStringDistanceHelper {

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

