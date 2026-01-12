package dev.goerner.geozen.model

import dev.goerner.geozen.model.simple_geometry.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull

class FeatureTest {
    @Test
    fun testGeometryConstructor() {
        val geometry: Geometry = Point(0.0, 0.0)
        val feature = Feature(null, geometry)

        assertNull(feature.id)
        assertEquals(geometry, feature.geometry)
        assertEquals(emptyMap<String, String>(), feature.properties)
    }

    @Test
    fun testIdAndGeometryConstructor() {
        val geometry: Geometry = Point(0.0, 0.0)
        val feature = Feature("123", geometry)

        assertEquals("123", feature.id)
        assertEquals(geometry, feature.geometry)
        assertEquals(emptyMap<String, String>(), feature.properties)
    }

    @Test
    fun testGeometryAndPropertiesConstructor() {
        val geometry: Geometry = Point(0.0, 0.0)
        val properties = mapOf(
            "key" to "value"
        )
        val feature = Feature(null, geometry, properties)

        assertNull(feature.id)
        assertEquals(geometry, feature.geometry)
        assertEquals(properties, feature.properties)
    }

    @Test
    fun testIdAndGeometryAndPropertiesConstructor() {
        val geometry: Geometry = Point(0.0, 0.0)
        val properties = mapOf(
            "key" to "value"
        )
        val feature = Feature("123", geometry, properties)

        assertEquals("123", feature.id)
        assertEquals(geometry, feature.geometry)
        assertEquals(properties, feature.properties)
    }
}
