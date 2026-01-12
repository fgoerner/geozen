package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineStringTest {
    @Test
    fun testCoordinatesConstructor() {
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        val lineString = LineString(coordinates)

        Assertions.assertEquals(2, lineString.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, lineString.coordinateReferenceSystem)
    }

    @Test
    fun testReferenceSystemConstructor() {
        val coordinates = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )

        val lineString = LineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(2, lineString.coordinates.size)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, lineString.coordinateReferenceSystem)
    }
}
