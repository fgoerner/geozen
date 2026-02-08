package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position

/**
 * Internal helper class for Point-to-Polygon distance calculations.
 *
 * This class encapsulates the shared containment logic that is common to both
 * ApproximateDistanceCalculator and PreciseDistanceCalculator. Only the distance
 * calculation differs between the two calculators.
 */
internal object PointToPolygonDistanceHelper {

    /**
     * Result of containment analysis for Point-to-Polygon.
     */
    sealed class ContainmentResult {
        /** Point is inside polygon (not in any hole) - distance is 0.0 */
        object InsidePolygon : ContainmentResult()

        /** Point is inside a hole - need to calculate distance to hole boundary */
        data class InsideHole(val holeRing: List<Position>) : ContainmentResult()

        /** Point is outside polygon - need to calculate distance to exterior ring */
        object OutsidePolygon : ContainmentResult()
    }

    /**
     * Analyzes the containment relationship between a point and a polygon.
     *
     * @param px the point's longitude
     * @param py the point's latitude
     * @param exteriorRing the polygon's exterior ring
     * @param interiorRings the polygon's interior rings (holes)
     * @return the containment result
     */
    fun analyzePointPolygonContainment(
        px: Double,
        py: Double,
        exteriorRing: List<Position>,
        interiorRings: List<List<Position>>
    ): ContainmentResult {
        // Check if point is inside the exterior ring
        val insideExterior = GeometricUtils.isPointInsideRing(px, py, exteriorRing)

        if (insideExterior) {
            // Check if point is inside any hole
            val holeContainingPoint = interiorRings.firstOrNull { hole ->
                GeometricUtils.isPointInsideRing(px, py, hole)
            }

            return if (holeContainingPoint != null) {
                ContainmentResult.InsideHole(holeContainingPoint)
            } else {
                ContainmentResult.InsidePolygon
            }
        }

        return ContainmentResult.OutsidePolygon
    }
}


