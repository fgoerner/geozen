package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PointTest {
    @Test
    fun testLongitudeAndLatitudeConstructor() {
        val point = Point(1.0, 2.0)

        Assertions.assertEquals(1.0, point.longitude)
        Assertions.assertEquals(2.0, point.latitude)
        Assertions.assertEquals(0.0, point.altitude)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, point.coordinateReferenceSystem)
    }

    @Test
    fun testLongitudeAndLatitudeAndAltitudeConstructor() {
        val point = Point(1.0, 2.0, 3.0)

        Assertions.assertEquals(1.0, point.longitude)
        Assertions.assertEquals(2.0, point.latitude)
        Assertions.assertEquals(3.0, point.altitude)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, point.coordinateReferenceSystem)
    }

    @Test
    fun testCoordinatesConstructor() {
        val coordinates = Position(1.0, 2.0, 3.0)
        val point = Point(coordinates)

        Assertions.assertEquals(coordinates, point.coordinates)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, point.coordinateReferenceSystem)
    }

    @Test
    fun testLongitudeAndLatitudeAndReferenceSystemConstructor() {
        val point = Point(1.0, 2.0, 0.0, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(1.0, point.longitude)
        Assertions.assertEquals(2.0, point.latitude)
        Assertions.assertEquals(0.0, point.altitude)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.coordinateReferenceSystem)
    }

    @Test
    fun testLongitudeAndLatitudeAndAltitudeAndReferenceSystemConstructor() {
        val point = Point(1.0, 2.0, 3.0, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(1.0, point.longitude)
        Assertions.assertEquals(2.0, point.latitude)
        Assertions.assertEquals(3.0, point.altitude)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.coordinateReferenceSystem)
    }

    @Test
    fun testCoordinatesAndReferenceSystemConstructor() {
        val coordinates = Position(1.0, 2.0, 3.0)
        val point = Point(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(coordinates, point.coordinates)
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.coordinateReferenceSystem)
    }
}
