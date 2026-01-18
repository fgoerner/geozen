package dev.goerner.geozen.model

import dev.goerner.geozen.model.simple_geometry.Point
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class FeatureTest : FunSpec({

    test("geometry constructor") {
        //given
        val geometry: Geometry = Point(0.0, 0.0)

        //when
        val feature = Feature(null, geometry)

        //then
        feature.id shouldBe null
        feature.geometry shouldBe geometry
        feature.properties shouldBe emptyMap<String, String>()
    }

    test("id and geometry constructor") {
        //given
        val geometry: Geometry = Point(0.0, 0.0)

        //when
        val feature = Feature("123", geometry)

        //then
        feature.id shouldBe "123"
        feature.geometry shouldBe geometry
        feature.properties shouldBe emptyMap<String, String>()
    }

    test("geometry and properties constructor") {
        //given
        val geometry: Geometry = Point(0.0, 0.0)
        val properties = mapOf(
            "key" to "value"
        )

        //when
        val feature = Feature(null, geometry, properties)

        //then
        feature.id shouldBe null
        feature.geometry shouldBe geometry
        feature.properties shouldBe properties
    }

    test("id and geometry and properties constructor") {
        //given
        val geometry: Geometry = Point(0.0, 0.0)
        val properties = mapOf(
            "key" to "value"
        )

        //when
        val feature = Feature("123", geometry, properties)

        //then
        feature.id shouldBe "123"
        feature.geometry shouldBe geometry
        feature.properties shouldBe properties
    }
})
