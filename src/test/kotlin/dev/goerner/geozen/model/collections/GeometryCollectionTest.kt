package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class GeometryCollectionTest : FunSpec({

    test("geometries constructor") {
        //given
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

        //when
        val geometryCollection = GeometryCollection(geometries)

        //then
        geometryCollection.geometries.size shouldBe 2
    }
})
