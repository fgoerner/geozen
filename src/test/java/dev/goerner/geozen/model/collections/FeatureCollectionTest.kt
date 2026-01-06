package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.simple_geometry.Point
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FeatureCollectionTest {
    @Test
    fun testFeaturesConstructor() {
        val features = listOf(
            Feature(null, Point(0.0, 0.0))
        )

        val featureCollection = FeatureCollection(features)

        Assertions.assertEquals(1, featureCollection.features.size)
    }
}
