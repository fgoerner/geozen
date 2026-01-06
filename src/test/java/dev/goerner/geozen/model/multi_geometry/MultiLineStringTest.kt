package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiLineStringTest {
    @Test
    fun testCoordinatesConstructor() {
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0)
            )
        )

        val multiLineString = MultiLineString(coordinates)

        Assertions.assertEquals(1, multiLineString.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.coordinateReferenceSystem)
    }

    @Test
    fun testCoordinatesAndReferenceSystemConstructor() {
        val coordinates = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0)
            )
        )

        val multiLineString = MultiLineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(1, multiLineString.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiLineString.coordinateReferenceSystem)
    }
}
