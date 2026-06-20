package dev.goerner.geozen.calc

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.Reprojector
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe

/**
 * Verifies that distance calculations produce the same results regardless of whether the input
 * geometries use WGS 84 or Web Mercator coordinates. Both calculators share the same reprojection
 * step in [AbstractDistanceCalculator], so tests run against both.
 *
 * Strategy: take geometry pairs whose WGS 84 distances are already established by
 * [ApproximateDistanceCalculatorTest] / [PreciseDistanceCalculatorTest], reproject the same
 * coordinates to Web Mercator, and assert the results match within floating-point rounding.
 */
class WebMercatorDistanceTest :
    FunSpec({
        val distanceTolerance = 0.01 // meters – accounts for double round-trip rounding

        // ── helpers ──────────────────────────────────────────────────────────────

        fun Position.toWm(): Position =
            Reprojector.reproject(
                this,
                CoordinateReferenceSystem.WGS_84,
                CoordinateReferenceSystem.WEB_MERCATOR,
            )

        // Reference WGS84 positions (shared across tests)
        val wgsA = Position(11.4694, 49.2965)
        val wgsB = Position(11.0549, 49.4532)
        val wgsC = Position(11.4432, 49.3429)
        val wgsD = Position(11.4463, 49.1877)
        val wgsE = Position(11.5161, 49.1239)

        val wgsPolygonExterior =
            listOf(
                Position(11.5, 49.3),
                Position(11.6, 49.3),
                Position(11.6, 49.4),
                Position(11.5, 49.4),
                Position(11.5, 49.3),
            )
        val wgsInsidePoint = Position(11.55, 49.35)

        // Reprojected Web Mercator equivalents
        val wmA = wgsA.toWm()
        val wmB = wgsB.toWm()
        val wmC = wgsC.toWm()
        val wmD = wgsD.toWm()
        val wmE = wgsE.toWm()
        val wmPolygonExterior = wgsPolygonExterior.map { it.toWm() }
        val wmInsidePoint = wgsInsidePoint.toWm()

        // ── ApproximateDistanceCalculator ─────────────────────────────────────────

        context("ApproximateDistanceCalculator") {
            test("Point to Point - both Web Mercator") {
                // given
                val p1 = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val p2 = Point(wmB, CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = ApproximateDistanceCalculator.calculate(p1, p2)

                // then
                distance shouldBe (34701.39385602524 plusOrMinus distanceTolerance)
            }

            test("Point to Point - mixed CRS") {
                // given
                val p1 = Point(wgsA)
                val p2 = Point(wmB, CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = ApproximateDistanceCalculator.calculate(p1, p2)

                // then
                distance shouldBe (34701.39385602524 plusOrMinus distanceTolerance)
            }

            test("Point to LineString - both Web Mercator") {
                // given
                val p = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val lineString =
                    LineString(listOf(wmC, wmD, wmE), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = ApproximateDistanceCalculator.calculate(p, lineString)

                // then
                distance shouldBe (1832.5414860629317 plusOrMinus distanceTolerance)
            }

            test("Point to Polygon - point outside, both Web Mercator") {
                // given
                val point = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val polygon =
                    Polygon(listOf(wmPolygonExterior), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = ApproximateDistanceCalculator.calculate(point, polygon)

                // then
                distance shouldBe (2252.7607736674404 plusOrMinus distanceTolerance)
            }

            test("Point to Polygon - point inside, both Web Mercator") {
                // given
                val point = Point(wmInsidePoint, CoordinateReferenceSystem.WEB_MERCATOR)
                val polygon =
                    Polygon(listOf(wmPolygonExterior), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = ApproximateDistanceCalculator.calculate(point, polygon)

                // then
                distance shouldBe 0.0
            }
        }

        // ── PreciseDistanceCalculator ─────────────────────────────────────────────

        context("PreciseDistanceCalculator") {
            test("Point to Point - both Web Mercator") {
                // given
                val p1 = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val p2 = Point(wmB, CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = PreciseDistanceCalculator.calculate(p1, p2)

                // then
                distance shouldBe (34782.42347014982 plusOrMinus distanceTolerance)
            }

            test("Point to Point - mixed CRS") {
                // given
                val p1 = Point(wgsA)
                val p2 = Point(wmB, CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = PreciseDistanceCalculator.calculate(p1, p2)

                // then
                distance shouldBe (34782.42347014982 plusOrMinus distanceTolerance)
            }

            test("Point to LineString - both Web Mercator") {
                // given
                val p = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val lineString =
                    LineString(listOf(wmC, wmD, wmE), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = PreciseDistanceCalculator.calculate(p, lineString)

                // then
                distance shouldBe (1837.9808889683015 plusOrMinus distanceTolerance)
            }

            test("Point to Polygon - point outside, both Web Mercator") {
                // given
                val point = Point(wmA, CoordinateReferenceSystem.WEB_MERCATOR)
                val polygon =
                    Polygon(listOf(wmPolygonExterior), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = PreciseDistanceCalculator.calculate(point, polygon)

                // then
                distance shouldBe (2259.4399787892717 plusOrMinus distanceTolerance)
            }

            test("Point to Polygon - point inside, both Web Mercator") {
                // given
                val point = Point(wmInsidePoint, CoordinateReferenceSystem.WEB_MERCATOR)
                val polygon =
                    Polygon(listOf(wmPolygonExterior), CoordinateReferenceSystem.WEB_MERCATOR)

                // when
                val distance = PreciseDistanceCalculator.calculate(point, polygon)

                // then
                distance shouldBe 0.0
            }
        }
    })
