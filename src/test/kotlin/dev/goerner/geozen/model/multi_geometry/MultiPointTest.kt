package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class MultiPointTest {
    @Test
    fun testCoordinatesConstructor() {
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        val multiPoint = MultiPoint(coordinates)

        Assertions.assertEquals(2, multiPoint.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, multiPoint.coordinateReferenceSystem)
    }

    @Test
    fun testCoordinatesAndReferenceSystemConstructor() {
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        val multiPoint = MultiPoint(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(2, multiPoint.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPoint.coordinateReferenceSystem)
    }
}
