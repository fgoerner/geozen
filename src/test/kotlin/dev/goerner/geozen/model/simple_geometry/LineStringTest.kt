package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class LineStringTest : FunSpec({

    test("coordinates constructor") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        //when
        val lineString = LineString(coordinates)

        //then
        lineString.coordinates.size shouldBe 2
        lineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("reference system constructor") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        //when
        val lineString = LineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //then
        lineString.coordinates.size shouldBe 2
        lineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("empty coordinates not allowed") {
        //given
        val coordinates = emptyList<Position>()

        //when & then
        shouldThrow<IllegalArgumentException> {
            LineString(coordinates)
        }
    }

    test("single position not allowed") {
        //given
        val coordinates = listOf(Position(1.0, 2.0))

        //when & then
        shouldThrow<IllegalArgumentException> {
            LineString(coordinates)
        }
    }

    test("exactly two positions allowed") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0),
            Position(3.0, 4.0)
        )

        //when
        val lineString = LineString(coordinates)

        //then
        lineString.coordinates.size shouldBe 2
    }

    test("multiple positions allowed") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0),
            Position(3.0, 4.0),
            Position(5.0, 6.0),
            Position(7.0, 8.0)
        )

        //when
        val lineString = LineString(coordinates)

        //then
        lineString.coordinates.size shouldBe 4
    }
})
