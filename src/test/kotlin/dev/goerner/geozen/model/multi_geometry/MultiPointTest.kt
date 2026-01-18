package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class MultiPointTest : FunSpec({

    test("coordinates constructor") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        //when
        val multiPoint = MultiPoint(coordinates)

        //then
        multiPoint.coordinates.size shouldBe 2
        multiPoint.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("coordinates and reference system constructor") {
        //given
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        //when
        val multiPoint = MultiPoint(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        //then
        multiPoint.coordinates.size shouldBe 2
        multiPoint.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }
})
