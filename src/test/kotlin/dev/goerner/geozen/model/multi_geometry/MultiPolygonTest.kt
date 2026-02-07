package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MultiPolygonTest : FunSpec({

    test("coordinates constructor") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(1.0, 2.0, 3.0),
                    Position(4.0, 5.0, 6.0),
                    Position(7.0, 8.0, 9.0),
                    Position(1.0, 2.0, 3.0)
                ),
                listOf(
                    Position(10.0, 11.0, 12.0),
                    Position(13.0, 14.0, 15.0),
                    Position(16.0, 17.0, 18.0),
                    Position(10.0, 11.0, 12.0)
                )
            )
        )

        //when
        val multiPolygon = MultiPolygon(coordinates)

        //then
        multiPolygon.coordinates.size shouldBe 1
        multiPolygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("coordinates and reference system constructor") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(1.0, 2.0, 3.0),
                    Position(4.0, 5.0, 6.0),
                    Position(7.0, 8.0, 9.0),
                    Position(1.0, 2.0, 3.0)
                ),
                listOf(
                    Position(10.0, 11.0, 12.0),
                    Position(13.0, 14.0, 15.0),
                    Position(16.0, 17.0, 18.0),
                    Position(10.0, 11.0, 12.0)
                )
            )
        )

        //when
        val multiPolygon = MultiPolygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //then
        multiPolygon.coordinates.size shouldBe 1
        multiPolygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("empty outer list allowed") {
        //given
        val coordinates = emptyList<List<List<Position>>>()

        //when
        val multiPolygon = MultiPolygon(coordinates)

        //then
        multiPolygon.coordinates.size shouldBe 0
    }

    test("polygon with no rings not allowed") {
        //given
        val coordinates = listOf(
            emptyList<List<Position>>()
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiPolygon(coordinates)
        }
    }

    test("polygon with ring having less than 4 positions not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(1.0, 0.0),
                    Position(0.0, 0.0)
                )
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiPolygon(coordinates)
        }
    }

    test("polygon with unclosed ring not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 10.0)  // Should be Position(0.0, 0.0)
                )
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiPolygon(coordinates)
        }
    }

    test("single valid polygon allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                )
            )
        )

        //when
        val multiPolygon = MultiPolygon(coordinates)

        //then
        multiPolygon.coordinates.size shouldBe 1
    }

    test("multiple valid polygons allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                )
            ),
            listOf(
                listOf(
                    Position(20.0, 20.0),
                    Position(30.0, 20.0),
                    Position(30.0, 30.0),
                    Position(20.0, 20.0)
                )
            )
        )

        //when
        val multiPolygon = MultiPolygon(coordinates)

        //then
        multiPolygon.coordinates.size shouldBe 2
    }

    test("polygon with interior ring allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                ),
                listOf(
                    Position(2.0, 2.0),
                    Position(8.0, 2.0),
                    Position(8.0, 8.0),
                    Position(2.0, 2.0)
                )
            )
        )

        //when
        val multiPolygon = MultiPolygon(coordinates)

        //then
        multiPolygon.coordinates.size shouldBe 1
        multiPolygon.coordinates[0].size shouldBe 2
    }

    test("second polygon with invalid ring not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                )
            ),
            listOf(
                listOf(
                    Position(20.0, 20.0),
                    Position(30.0, 20.0),
                    Position(20.0, 20.0)  // Only 3 positions
                )
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiPolygon(coordinates)
        }
    }

    test("interior ring unclosed not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                ),
                listOf(
                    Position(2.0, 2.0),
                    Position(8.0, 2.0),
                    Position(8.0, 8.0),
                    Position(2.0, 8.0)  // Should be Position(2.0, 2.0)
                )
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiPolygon(coordinates)
        }
    }
})
