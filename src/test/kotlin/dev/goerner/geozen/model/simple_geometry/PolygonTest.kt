package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PolygonTest : FunSpec({

    test("exterior ring constructor") {
        //given
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        //when
        val polygon = Polygon(coordinates)

        //then
        polygon.coordinates.size shouldBe 1
        polygon.coordinates[0] shouldBe exteriorRing
        polygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("exterior ring and reference system constructor") {
        //given
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        //when
        val polygon = Polygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //then
        polygon.coordinates.size shouldBe 1
        polygon.coordinates[0] shouldBe exteriorRing
        polygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("get exterior ring") {
        //given
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        //when
        val polygon = Polygon(coordinates)

        //then
        polygon.exteriorRing shouldBe exteriorRing
    }

    test("empty coordinates not allowed") {
        //given
        val coordinates = emptyList<List<Position>>()

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("ring with less than 4 positions not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0),
                Position(3.0, 4.0),
                Position(1.0, 2.0)
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("ring with exactly 3 positions not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0),
                Position(3.0, 4.0),
                Position(5.0, 6.0)
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("unclosed ring not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(0.0, 0.0),
                Position(10.0, 0.0),
                Position(10.0, 10.0),
                Position(0.0, 10.0)  // Should be Position(0.0, 0.0) to be closed
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("exterior ring with exactly 4 positions allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(0.0, 0.0),
                Position(10.0, 0.0),
                Position(10.0, 10.0),
                Position(0.0, 0.0)
            )
        )

        //when
        val polygon = Polygon(coordinates)

        //then
        polygon.coordinates.size shouldBe 1
        polygon.coordinates[0].size shouldBe 4
    }

    test("polygon with interior ring allowed") {
        //given
        val coordinates = listOf(
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

        //when
        val polygon = Polygon(coordinates)

        //then
        polygon.coordinates.size shouldBe 2
    }

    test("interior ring with insufficient positions not allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(0.0, 0.0),
                Position(10.0, 0.0),
                Position(10.0, 10.0),
                Position(0.0, 0.0)
            ),
            listOf(
                Position(2.0, 2.0),
                Position(8.0, 2.0),
                Position(2.0, 2.0)
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("interior ring unclosed not allowed") {
        //given
        val coordinates = listOf(
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
                Position(2.0, 8.0)  // Should be Position(2.0, 2.0) to be closed
            )
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            Polygon(coordinates)
        }
    }

    test("multiple interior rings allowed") {
        //given
        val coordinates = listOf(
            listOf(
                Position(0.0, 0.0),
                Position(20.0, 0.0),
                Position(20.0, 20.0),
                Position(0.0, 0.0)
            ),
            listOf(
                Position(2.0, 2.0),
                Position(8.0, 2.0),
                Position(8.0, 8.0),
                Position(2.0, 2.0)
            ),
            listOf(
                Position(12.0, 12.0),
                Position(18.0, 12.0),
                Position(18.0, 18.0),
                Position(12.0, 12.0)
            )
        )

        //when
        val polygon = Polygon(coordinates)

        //then
        polygon.coordinates.size shouldBe 3
    }
})
