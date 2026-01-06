package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiPolygonTest {
    @Test
    fun testCoordinatesConstructor() {
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

        val multiPolygon = MultiPolygon(coordinates)

        Assertions.assertEquals(1, multiPolygon.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, multiPolygon.coordinateReferenceSystem)
    }

    @Test
    fun testCoordinatesAndReferenceSystemConstructor() {
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

        val multiPolygon = MultiPolygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(1, multiPolygon.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPolygon.coordinateReferenceSystem)
    }
}
