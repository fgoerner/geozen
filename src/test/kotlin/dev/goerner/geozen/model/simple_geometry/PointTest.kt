package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PointTest : FunSpec({

    test("longitude and latitude constructor") {
        //given
        val point = Point(1.0, 2.0)

        //when
        // constructor

        //then
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        point.altitude shouldBe 0.0
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("longitude latitude and altitude constructor") {
        //given
        val point = Point(1.0, 2.0, 3.0)

        //when
        // constructor

        //then
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        point.altitude shouldBe 3.0
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("coordinates constructor") {
        //given
        val coordinates = Position(1.0, 2.0, 3.0)
        val point = Point(coordinates)

        //when
        // constructor

        //then
        point.coordinates shouldBe coordinates
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("longitude latitude and reference system constructor") {
        //given
        val point = Point(1.0, 2.0, 0.0, CoordinateReferenceSystem.WEB_MERCATOR)

        //when
        // constructor

        //then
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        point.altitude shouldBe 0.0
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("longitude latitude altitude and reference system constructor") {
        //given
        val point = Point(1.0, 2.0, 3.0, CoordinateReferenceSystem.WEB_MERCATOR)

        //when
        // constructor

        //then
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        point.altitude shouldBe 3.0
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("coordinates and reference system constructor") {
        //given
        val coordinates = Position(1.0, 2.0, 3.0)
        val point = Point(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //when
        // constructor

        //then
        point.coordinates shouldBe coordinates
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }
})
