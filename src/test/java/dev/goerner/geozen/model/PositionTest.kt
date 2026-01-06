package dev.goerner.geozen.model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PositionTest {
    @Test
    fun testLongitudeAndLatitudeConstructor() {
        val position = Position(1.0, 2.0)

        Assertions.assertEquals(1.0, position.longitude)
        Assertions.assertEquals(2.0, position.latitude)
        Assertions.assertEquals(0.0, position.altitude)
    }

    @Test
    fun testLongitudeAndLatitudeAndAltitudeConstructor() {
        val position = Position(1.0, 2.0, 3.0)

        Assertions.assertEquals(1.0, position.longitude)
        Assertions.assertEquals(2.0, position.latitude)
        Assertions.assertEquals(3.0, position.altitude)
    }

    @Test
    fun testGetLongitude() {
        val position = Position(1.0, 2.0, 3.0)

        Assertions.assertEquals(1.0, position.longitude)
    }

    @Test
    fun testGetLatitude() {
        val position = Position(1.0, 2.0, 3.0)

        Assertions.assertEquals(2.0, position.latitude)
    }

    @Test
    fun testGetAltitude() {
        val position = Position(1.0, 2.0, 3.0)

        Assertions.assertEquals(3.0, position.altitude)
    }
}
