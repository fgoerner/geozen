package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class GeometryCollectionTest {
    @Test
    fun testGeometriesConstructor() {
        val point: Geometry = Point(0.0, 0.0)
        val geometries = listOf(
            point,
            LineString(
                listOf(
                    Position(1.0, 1.0),
                    Position(2.0, 2.0)
                )
            )
        )

        val geometryCollection = GeometryCollection(geometries)

        Assertions.assertEquals(2, geometryCollection.geometries.size)
    }
}
