package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class PolygonTest {
    @Test
    fun testExteriorRingConstructor() {
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        val polygon = Polygon(coordinates)

        Assertions.assertEquals(1, polygon.coordinates.size)
        Assertions.assertEquals(exteriorRing, polygon.coordinates[0])
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, polygon.coordinateReferenceSystem)
    }

    @Test
    fun testExteriorRingAndReferenceSystemConstructor() {
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        val polygon = Polygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR)

        Assertions.assertEquals(1, polygon.coordinates.size)
        Assertions.assertEquals(exteriorRing, polygon.coordinates[0])
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.coordinateReferenceSystem)
    }

    @Test
    fun testGetExteriorRing() {
        val exteriorRing = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0),
            Position(7.0, 8.0, 9.0),
            Position(1.0, 2.0, 3.0)
        )
        val coordinates = listOf(
            exteriorRing
        )

        val polygon = Polygon(coordinates)

        Assertions.assertEquals(exteriorRing, polygon.exteriorRing)
    }
}
