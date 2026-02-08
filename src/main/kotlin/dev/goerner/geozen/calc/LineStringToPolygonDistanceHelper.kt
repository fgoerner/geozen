package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point

/**
 * Internal helper class for LineString-to-Polygon distance calculations.
 *
 * This class encapsulates the shared logic (Phase 1: intersection detection and Phase 2: containment checks)
 * that is common to both ApproximateDistanceCalculator and PreciseDistanceCalculator.
 *
 * Only Phase 3 (distance calculation) differs between the two calculators, which is handled by
 * passing different distance calculation functions.
 */
internal object LineStringToPolygonDistanceHelper {

    /**
     * Result of Phase 1 and Phase 2 analysis.
     */
    sealed class AnalysisResult {
        /** LineString intersects polygon boundary - distance is 0.0 */
        object Intersection : AnalysisResult()

        /** All LineString vertices are inside polygon (not in any hole) - distance is 0.0 */
        object FullyContained : AnalysisResult()

        /** At least one LineString vertex is inside a hole - need to calculate distance to hole boundary */
        data class InHole(val holeContainingVertex: List<Position>) : AnalysisResult()

        /** No intersection or containment - need to calculate general distance */
        object NoIntersectionOrContainment : AnalysisResult()
    }

    /**
     * Performs Phase 1 (intersection detection) and Phase 2 (containment analysis).
     *
     * @param lineStringPositions the LineString vertices
     * @param exteriorRing the polygon's exterior ring
     * @param interiorRings the polygon's interior rings (holes)
     * @return the analysis result indicating which phase 3 calculation (if any) is needed
     */
    fun analyzeLineStringPolygonRelationship(
        lineStringPositions: List<Position>,
        exteriorRing: List<Position>,
        interiorRings: List<List<Position>>
    ): AnalysisResult {
        // Phase 1: Check for segment intersections between LineString and any polygon ring
        for (i in 0 until lineStringPositions.size - 1) {
            val lsSegStart = lineStringPositions[i]
            val lsSegEnd = lineStringPositions[i + 1]

            // Check intersection with exterior ring
            for (j in 0 until exteriorRing.size - 1) {
                val ringSegStart = exteriorRing[j]
                val ringSegEnd = exteriorRing[j + 1]

                if (GeometricUtils.doSegmentsIntersect(lsSegStart, lsSegEnd, ringSegStart, ringSegEnd)) {
                    return AnalysisResult.Intersection
                }
            }

            // Check intersection with interior rings (holes)
            for (hole in interiorRings) {
                for (j in 0 until hole.size - 1) {
                    val holeSegStart = hole[j]
                    val holeSegEnd = hole[j + 1]

                    if (GeometricUtils.doSegmentsIntersect(lsSegStart, lsSegEnd, holeSegStart, holeSegEnd)) {
                        return AnalysisResult.Intersection
                    }
                }
            }
        }

        // Phase 2: Check containment - analyze LineString vertices
        var allVerticesInsidePolygon = true
        var anyVertexInHole = false
        var holeContainingVertex: List<Position>? = null

        for (position in lineStringPositions) {
            val insideExterior = GeometricUtils.isPointInsideRing(position.longitude, position.latitude, exteriorRing)

            if (!insideExterior) {
                allVerticesInsidePolygon = false
                break
            }

            // Check if this vertex is in any hole
            val hole = interiorRings.firstOrNull { h ->
                GeometricUtils.isPointInsideRing(position.longitude, position.latitude, h)
            }
            if (hole != null) {
                anyVertexInHole = true
                holeContainingVertex = hole
                allVerticesInsidePolygon = false
                break
            }
        }

        // Determine the result based on containment analysis
        return when {
            allVerticesInsidePolygon -> AnalysisResult.FullyContained
            anyVertexInHole && holeContainingVertex != null -> AnalysisResult.InHole(holeContainingVertex)
            else -> AnalysisResult.NoIntersectionOrContainment
        }
    }

    /**
     * Calculates distance when LineString is inside a hole (Phase 2 case).
     *
     * @param lineStringPositions the LineString vertices
     * @param holeRing the hole's boundary ring
     * @param calculateMinDistanceToPositions function to calculate minimum distance from a point to a sequence of positions
     * @return the minimum distance
     */
    fun calculateDistanceForHoleCase(
        lineStringPositions: List<Position>,
        holeRing: List<Position>,
        calculateMinDistanceToPositions: (Point, List<Position>) -> Double
    ): Double {
        // Calculate distance from LineString vertices to hole boundary
        val minFromLineStringToHole = lineStringPositions.minOf { position ->
            calculateMinDistanceToPositions(Point(position), holeRing)
        }

        // Calculate distance from hole boundary vertices to LineString
        val minFromHoleToLineString = holeRing.minOf { position ->
            calculateMinDistanceToPositions(Point(position), lineStringPositions)
        }

        return minOf(minFromLineStringToHole, minFromHoleToLineString)
    }

    /**
     * Calculates distance for the general case (Phase 3) where there's no intersection or containment.
     *
     * @param lineStringPositions the LineString vertices
     * @param rings all polygon rings (exterior + interior)
     * @param exteriorRing the polygon's exterior ring
     * @param interiorRings the polygon's interior rings (holes)
     * @param calculateMinDistanceToPositions function to calculate minimum distance from a point to a sequence of positions
     * @return the minimum distance
     */
    fun calculateDistanceForGeneralCase(
        lineStringPositions: List<Position>,
        rings: List<List<Position>>,
        exteriorRing: List<Position>,
        interiorRings: List<List<Position>>,
        calculateMinDistanceToPositions: (Point, List<Position>) -> Double
    ): Double {
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
}


