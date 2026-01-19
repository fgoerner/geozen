package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
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
})
