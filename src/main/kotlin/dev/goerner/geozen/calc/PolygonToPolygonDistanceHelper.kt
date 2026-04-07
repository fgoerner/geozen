package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point

/**
 * Internal helper class for Polygon-to-Polygon distance calculations.
 *
 * This class encapsulates the shared logic (Phase 1: intersection detection and Phase 2: containment checks)
 * that is common to both ApproximateDistanceCalculator and PreciseDistanceCalculator.
 *
 * Only Phase 3 (distance calculation) differs between the two calculators, which is handled by
 * passing different distance calculation functions.
 */
internal object PolygonToPolygonDistanceHelper {

    /**
     * Result of Phase 1 and Phase 2 analysis.
     */
    sealed class AnalysisResult {
        /** Polygons have intersecting boundaries - distance is 0.0 */
        object Intersection : AnalysisResult()

        /** Polygon1 is fully contained within Polygon2 (not in any hole) - distance is 0.0 */
        object Polygon1ContainedInPolygon2 : AnalysisResult()

        /** Polygon2 is fully contained within Polygon1 (not in any hole) - distance is 0.0 */
        object Polygon2ContainedInPolygon1 : AnalysisResult()

        /** Polygon1 has vertices inside a hole of Polygon2 - need to calculate distance to hole boundary */
        data class Polygon1InHoleOfPolygon2(val holeRing: List<Position>) : AnalysisResult()

        /** Polygon2 has vertices inside a hole of Polygon1 - need to calculate distance to hole boundary */
        data class Polygon2InHoleOfPolygon1(val holeRing: List<Position>) : AnalysisResult()

        /** No intersection or containment - need to calculate general distance */
        object NoIntersectionOrContainment : AnalysisResult()
    }

    /**
     * Performs Phase 1 (intersection detection) and Phase 2 (containment analysis).
     *
     * @param rings1 all rings of polygon1 (exterior + interior)
     * @param rings2 all rings of polygon2 (exterior + interior)
     * @param exteriorRing1 polygon1's exterior ring
     * @param interiorRings1 polygon1's interior rings (holes)
     * @param exteriorRing2 polygon2's exterior ring
     * @param interiorRings2 polygon2's interior rings (holes)
     * @return the analysis result indicating which phase 3 calculation (if any) is needed
     */
    fun analyzePolygonPolygonRelationship(
        rings1: List<List<Position>>,
        rings2: List<List<Position>>,
        exteriorRing1: List<Position>,
        interiorRings1: List<List<Position>>,
        exteriorRing2: List<Position>,
        interiorRings2: List<List<Position>>
    ): AnalysisResult {
        // Phase 1: Check for segment intersections between any rings of both polygons
        for (ring1 in rings1) {
            for (ring2 in rings2) {
                if (GeometricUtils.doPolylineSegmentsIntersect(ring1, ring2)) {
                    return AnalysisResult.Intersection
                }
            }
        }

        // Phase 2: Check containment relationships
        // Check if any vertex of polygon1 is contained in polygon2
        when (val r = GeometricUtils.checkVertexContainment(exteriorRing1, exteriorRing2, interiorRings2)) {
            is GeometricUtils.VertexContainmentResult.FullyContained -> return AnalysisResult.Polygon1ContainedInPolygon2
            is GeometricUtils.VertexContainmentResult.InHole         -> return AnalysisResult.Polygon1InHoleOfPolygon2(r.holeRing)
            is GeometricUtils.VertexContainmentResult.NotContained   -> Unit // continue
        }

        // Check if any vertex of polygon2 is contained in polygon1
        when (val r = GeometricUtils.checkVertexContainment(exteriorRing2, exteriorRing1, interiorRings1)) {
            is GeometricUtils.VertexContainmentResult.FullyContained -> return AnalysisResult.Polygon2ContainedInPolygon1
            is GeometricUtils.VertexContainmentResult.InHole         -> return AnalysisResult.Polygon2InHoleOfPolygon1(r.holeRing)
            is GeometricUtils.VertexContainmentResult.NotContained   -> Unit // continue
        }

        return AnalysisResult.NoIntersectionOrContainment
    }


    /**
     * Calculates distance when one polygon is inside a hole of another polygon.
     *
     * @param polygonRings all rings of the polygon that is in the hole
     * @param holeRing the hole's boundary ring
     * @param calculateMinDistanceToPositions function to calculate minimum distance from a point to a sequence of positions
     * @return the minimum distance
     */
    fun calculateDistanceForHoleCase(
        polygonRings: List<List<Position>>,
        holeRing: List<Position>,
        calculateMinDistanceToPositions: (Point, List<Position>) -> Double
    ): Double {
        // Calculate distance from all polygon ring vertices to hole boundary
        val allPolygonVertices = polygonRings.flatten()
        val minFromPolygonToHole = allPolygonVertices.minOf { position ->
            calculateMinDistanceToPositions(Point(position), holeRing)
        }

        // Calculate distance from hole boundary vertices to all polygon rings
        val minFromHoleToPolygon = holeRing.minOf { position ->
            polygonRings.minOf { ring ->
                calculateMinDistanceToPositions(Point(position), ring)
            }
        }

        return minOf(minFromPolygonToHole, minFromHoleToPolygon)
    }

    /**
     * Calculates distance for the general case (Phase 3) where there's no intersection or containment.
     *
     * This method calculates bidirectional distances:
     * 1. From all vertices of polygon1 to all rings of polygon2
     * 2. From all vertices of polygon2 to all rings of polygon1
     *
     * @param rings1 all rings of polygon1
     * @param rings2 all rings of polygon2
     * @param calculateMinDistanceToPositions function to calculate minimum distance from a point to a sequence of positions
     * @return the minimum distance
     */
    fun calculateDistanceForGeneralCase(
        rings1: List<List<Position>>,
        rings2: List<List<Position>>,
        calculateMinDistanceToPositions: (Point, List<Position>) -> Double
    ): Double {
        // Calculate minimum distance from all polygon1 vertices to all polygon2 rings
        val allVertices1 = rings1.flatten()
        val minFromPolygon1ToPolygon2 = allVertices1.minOf { position ->
            rings2.minOf { ring ->
                calculateMinDistanceToPositions(Point(position), ring)
            }
        }

        // Calculate minimum distance from all polygon2 vertices to all polygon1 rings
        val allVertices2 = rings2.flatten()
        val minFromPolygon2ToPolygon1 = allVertices2.minOf { position ->
            rings1.minOf { ring ->
                calculateMinDistanceToPositions(Point(position), ring)
            }
        }

        return minOf(minFromPolygon1ToPolygon2, minFromPolygon2ToPolygon1)
    }
}

