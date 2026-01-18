package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.simple_geometry.Point
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FeatureCollectionTest : FunSpec({

    test("features constructor") {
        //given
        val features = listOf(
            Feature(null, Point(0.0, 0.0))
        )

        //when
        val featureCollection = FeatureCollection(features)

        //then
        featureCollection.features.size shouldBe 1
    }
})
