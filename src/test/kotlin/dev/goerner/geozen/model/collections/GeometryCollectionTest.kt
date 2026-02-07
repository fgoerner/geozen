package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import io.kotest.assertions.throwables.shouldThrow
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

    test("cannot instantiate with mismatched CRSs") {
        //given
        val pointWgs84: Geometry = Point(0.0, 0.0, coordinateReferenceSystem = CoordinateReferenceSystem.WGS_84)
        val lineStringWebMercator: Geometry = LineString(
            listOf(
                Position(1.0, 1.0),
                Position(2.0, 2.0)
            ),
            coordinateReferenceSystem = CoordinateReferenceSystem.WEB_MERCATOR
        )
        val geometries = listOf(pointWgs84, lineStringWebMercator)

        //when & then
        shouldThrow<IllegalArgumentException> {
            GeometryCollection(geometries, CoordinateReferenceSystem.WGS_84)
        }
    }
})
