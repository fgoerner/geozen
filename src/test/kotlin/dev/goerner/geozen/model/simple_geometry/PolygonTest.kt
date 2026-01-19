package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
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
})
