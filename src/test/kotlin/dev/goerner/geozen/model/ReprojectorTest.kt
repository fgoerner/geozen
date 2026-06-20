package dev.goerner.geozen.model

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs

class ReprojectorTest :
    FunSpec({
        val tolerance = 1e-6

        // ── Identity ─────────────────────────────────────────────────────────────

        test("WGS84 to WGS84 returns same Position instance") {
            // given
            val pos = Position(11.0, 48.0, 100.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WGS_84,
                )

            // then
            result shouldBeSameInstanceAs pos
        }

        test("Web Mercator to Web Mercator returns same Position instance") {
            // given
            val pos = Position(1223685.0, 6140770.0, 100.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )

            // then
            result shouldBeSameInstanceAs pos
        }

        // ── WGS84 → Web Mercator ─────────────────────────────────────────────────

        test("WGS84 origin maps to Web Mercator origin") {
            // given
            val pos = Position(0.0, 0.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )

            // then
            result.longitude shouldBe (0.0 plusOrMinus tolerance)
            result.latitude shouldBe (0.0 plusOrMinus tolerance)
        }

        test("WGS84 to Web Mercator known value") {
            // given
            // lon=11.0°, lat=0.0° (equatorial): x = 6378137 * toRadians(11), y = 0
            val pos = Position(11.0, 0.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )

            // then
            result.longitude shouldBe (1224514.3987260093 plusOrMinus 0.001)
            result.latitude shouldBe (0.0 plusOrMinus 0.001)
        }

        test("WGS84 to Web Mercator preserves altitude") {
            // given
            val pos = Position(11.0, 48.0, 500.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )

            // then
            result.altitude shouldBe 500.0
        }

        test("WGS84 to Web Mercator rejects latitude above 85.051129") {
            // given
            val pos = Position(0.0, 85.051130)

            // when / then
            shouldThrow<IllegalArgumentException> {
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )
            }
        }

        test("WGS84 to Web Mercator rejects latitude below -85.051129") {
            // given
            val pos = Position(0.0, -85.051130)

            // when / then
            shouldThrow<IllegalArgumentException> {
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )
            }
        }

        // ── Web Mercator → WGS84 ─────────────────────────────────────────────────

        test("Web Mercator origin maps to WGS84 origin") {
            // given
            val pos = Position(0.0, 0.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WGS_84,
                )

            // then
            result.longitude shouldBe (0.0 plusOrMinus tolerance)
            result.latitude shouldBe (0.0 plusOrMinus tolerance)
        }

        test("Web Mercator to WGS84 known value") {
            // given
            // x = 6378137 * toRadians(11) ≈ 1224514.399, y = 0  →  lon=11°, lat=0°
            val pos = Position(1224514.3987260093, 0.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WGS_84,
                )

            // then
            result.longitude shouldBe (11.0 plusOrMinus tolerance)
            result.latitude shouldBe (0.0 plusOrMinus tolerance)
        }

        test("Web Mercator to WGS84 preserves altitude") {
            // given
            val pos = Position(0.0, 0.0, 250.0)

            // when
            val result =
                Reprojector.reproject(
                    pos,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WGS_84,
                )

            // then
            result.altitude shouldBe 250.0
        }

        // ── Round-trips ──────────────────────────────────────────────────────────

        test("WGS84 to Web Mercator round-trip") {
            // given
            val original = Position(11.0, 48.0, 100.0)

            // when
            val webMercator =
                Reprojector.reproject(
                    original,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )
            val roundTrip =
                Reprojector.reproject(
                    webMercator,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WGS_84,
                )

            // then
            roundTrip.longitude shouldBe (original.longitude plusOrMinus tolerance)
            roundTrip.latitude shouldBe (original.latitude plusOrMinus tolerance)
            roundTrip.altitude shouldBe original.altitude
        }

        test("Web Mercator to WGS84 round-trip") {
            // given
            val original = Position(1223685.1096, 6106854.8349, 100.0)

            // when
            val wgs84 =
                Reprojector.reproject(
                    original,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                    CoordinateReferenceSystem.WGS_84,
                )
            val roundTrip =
                Reprojector.reproject(
                    wgs84,
                    CoordinateReferenceSystem.WGS_84,
                    CoordinateReferenceSystem.WEB_MERCATOR,
                )

            // then
            roundTrip.longitude shouldBe (original.longitude plusOrMinus tolerance)
            roundTrip.latitude shouldBe (original.latitude plusOrMinus tolerance)
            roundTrip.altitude shouldBe original.altitude
        }
    })
