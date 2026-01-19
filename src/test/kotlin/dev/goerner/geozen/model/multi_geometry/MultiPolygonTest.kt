package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
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
})
