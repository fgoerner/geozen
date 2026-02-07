package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MultiLineStringTest : FunSpec({

    test("coordinates constructor") {
        //given
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0)
            )
        )

        //when
        val multiLineString = MultiLineString(coordinates)

        //then
        multiLineString.coordinates.size shouldBe 1
        multiLineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("coordinates and reference system constructor") {
        //given
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0)
            )
        )

        //when
        val multiLineString = MultiLineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //then
        multiLineString.coordinates.size shouldBe 1
        multiLineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("empty outer list allowed") {
        //given
        val coordinates = emptyList<List<Position>>()

        //when
        val multiLineString = MultiLineString(coordinates)

        //then
        multiLineString.coordinates.size shouldBe 0
    }

    test("inner list with empty positions not allowed") {
        //given
        val coordinates = listOf(emptyList<Position>())

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiLineString(coordinates)
        }
    }

    test("inner list with single position not allowed") {
        //given
        val coordinates = listOf(
            listOf(Position(1.0, 2.0))
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiLineString(coordinates)
        }
    }

    test("multiple inner lists with valid positions allowed") {
        //given
        val coordinates = listOf(
            listOf(Position(1.0, 2.0), Position(3.0, 4.0)),
            listOf(Position(5.0, 6.0), Position(7.0, 8.0)),
            listOf(Position(9.0, 10.0), Position(11.0, 12.0), Position(13.0, 14.0))
        )

        //when
        val multiLineString = MultiLineString(coordinates)

        //then
        multiLineString.coordinates.size shouldBe 3
    }

    test("second inner list with single position not allowed") {
        //given
        val coordinates = listOf(
            listOf(Position(1.0, 2.0), Position(3.0, 4.0)),
            listOf(Position(5.0, 6.0)) // Invalid
        )

        //when & then
        shouldThrow<IllegalArgumentException> {
            MultiLineString(coordinates)
        }
    }
})
