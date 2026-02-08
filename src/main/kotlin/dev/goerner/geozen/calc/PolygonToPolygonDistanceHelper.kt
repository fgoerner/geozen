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
                if (doRingsIntersect(ring1, ring2)) {
                    return AnalysisResult.Intersection
                }
            }
        }

        // Phase 2: Check containment relationships
        // Check if any vertex of polygon1 is contained in polygon2
        val polygon1VerticesInPolygon2 = checkPolygonContainment(
            exteriorRing1,
            exteriorRing2,
            interiorRings2
        )

        when (polygon1VerticesInPolygon2) {
            is ContainmentCheck.FullyContained -> return AnalysisResult.Polygon1ContainedInPolygon2
            is ContainmentCheck.InHole -> return AnalysisResult.Polygon1InHoleOfPolygon2(polygon1VerticesInPolygon2.holeRing)
            is ContainmentCheck.NotContained -> {} // Continue checking
        }

        // Check if any vertex of polygon2 is contained in polygon1
        val polygon2VerticesInPolygon1 = checkPolygonContainment(
            exteriorRing2,
            exteriorRing1,
            interiorRings1
        )

        when (polygon2VerticesInPolygon1) {
            is ContainmentCheck.FullyContained -> return AnalysisResult.Polygon2ContainedInPolygon1
            is ContainmentCheck.InHole -> return AnalysisResult.Polygon2InHoleOfPolygon1(polygon2VerticesInPolygon1.holeRing)
            is ContainmentCheck.NotContained -> {} // Continue to general case
        }

        return AnalysisResult.NoIntersectionOrContainment
    }

    /**
     * Result of containment check for a polygon within another polygon.
     */
    private sealed class ContainmentCheck {
        /** All vertices are inside the polygon (not in any hole) */
        object FullyContained : ContainmentCheck()

        /** At least one vertex is inside a hole */
        data class InHole(val holeRing: List<Position>) : ContainmentCheck()

        /** Vertices are not contained */
        object NotContained : ContainmentCheck()
    }

    /**
     * Checks if any ring of one polygon intersects with another ring.
     *
     * @param ring1 first ring
     * @param ring2 second ring
     * @return true if rings intersect, false otherwise
     */
    private fun doRingsIntersect(ring1: List<Position>, ring2: List<Position>): Boolean {
        for (i in 0 until ring1.size - 1) {
            val seg1Start = ring1[i]
            val seg1End = ring1[i + 1]

            for (j in 0 until ring2.size - 1) {
                val seg2Start = ring2[j]
                val seg2End = ring2[j + 1]

                if (GeometricUtils.doSegmentsIntersect(seg1Start, seg1End, seg2Start, seg2End)) {
                    return true
                }
            }
        }
        return false
    }

    /**
     * Checks containment of vertices from one polygon within another polygon.
     *
     * @param testRing the exterior ring of the polygon to test
     * @param containerExteriorRing the exterior ring of the container polygon
     * @param containerInteriorRings the holes of the container polygon
     * @return containment check result
     */
    private fun checkPolygonContainment(
        testRing: List<Position>,
        containerExteriorRing: List<Position>,
        containerInteriorRings: List<List<Position>>
    ): ContainmentCheck {
        var allVerticesInsidePolygon = true
        var anyVertexInHole = false
        var holeContainingVertex: List<Position>? = null

        for (position in testRing) {
            val insideExterior = GeometricUtils.isPointInsideRing(
                position.longitude,
                position.latitude,
                containerExteriorRing
            )

            if (!insideExterior) {
                allVerticesInsidePolygon = false
                break
            }

            // Check if this vertex is in any hole
            val hole = containerInteriorRings.firstOrNull { h ->
                GeometricUtils.isPointInsideRing(position.longitude, position.latitude, h)
            }
            if (hole != null) {
                anyVertexInHole = true
                holeContainingVertex = hole
                allVerticesInsidePolygon = false
                break
            }
        }

        return when {
            allVerticesInsidePolygon -> ContainmentCheck.FullyContained
            anyVertexInHole && holeContainingVertex != null -> ContainmentCheck.InHole(holeContainingVertex)
            else -> ContainmentCheck.NotContained
        }
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

