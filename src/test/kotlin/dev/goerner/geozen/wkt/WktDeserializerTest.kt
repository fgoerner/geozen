package dev.goerner.geozen.wkt

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class WktDeserializerTest : FunSpec({

    // Point Tests
    test("deserialize point") {
        //given
        val wkt = "POINT (10.5 20.3)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe 10.5
        point.coordinates.latitude shouldBe 20.3
        point.coordinates.altitude shouldBe 0.0
    }

    test("deserialize point with altitude") {
        //given
        val wkt = "POINT (10.5 20.3 100.0)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe 10.5
        point.coordinates.latitude shouldBe 20.3
        point.coordinates.altitude shouldBe 100.0
    }

    test("deserialize point case insensitive") {
        //given
        val wkt = "point (10.5 20.3)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe 10.5
        point.coordinates.latitude shouldBe 20.3
    }

    test("deserialize point EWKT WGS84") {
        //given
        val wkt = "SRID=4326;POINT (10.5 20.3)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe 10.5
        point.coordinates.latitude shouldBe 20.3
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("deserialize point EWKT Web Mercator") {
        //given
        val wkt = "SRID=3857;POINT (10.5 20.3)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("deserialize point empty not supported") {
        //given
        val wkt = "POINT EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize point negative coordinates") {
        //given
        val wkt = "POINT (-122.4194 37.7749)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val point = geom.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe -122.4194
        point.coordinates.latitude shouldBe 37.7749
    }

    // LineString Tests
    test("deserialize LineString") {
        //given
        val wkt = "LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val lineString = geom.shouldBeInstanceOf<LineString>()

        //then
        lineString.coordinates.size shouldBe 3
        lineString.coordinates[0].longitude shouldBe 10.0
        lineString.coordinates[0].latitude shouldBe 20.0
        lineString.coordinates[2].longitude shouldBe 50.0
        lineString.coordinates[2].latitude shouldBe 60.0
    }

    test("deserialize LineString empty not supported") {
        //given
        val wkt = "LINESTRING EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize LineString with altitude") {
        //given
        val wkt = "LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val lineString = geom.shouldBeInstanceOf<LineString>()

        //then
        lineString.coordinates.size shouldBe 2
        lineString.coordinates[0].altitude shouldBe 5.0
        lineString.coordinates[1].altitude shouldBe 10.0
    }

    test("deserialize LineString EWKT") {
        //given
        val wkt = "SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val lineString = geom.shouldBeInstanceOf<LineString>()

        //then
        lineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    // Polygon Tests
    test("deserialize Polygon") {
        //given
        val wkt = "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val polygon = geom.shouldBeInstanceOf<Polygon>()

        //then
        polygon.coordinates.size shouldBe 1
        polygon.coordinates[0].size shouldBe 5
        polygon.coordinates[0][0].longitude shouldBe 0.0
        polygon.coordinates[0][0].latitude shouldBe 0.0
    }

    test("deserialize Polygon with hole") {
        //given
        val wkt = "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val polygon = geom.shouldBeInstanceOf<Polygon>()

        //then
        polygon.coordinates.size shouldBe 2
        polygon.coordinates[0].size shouldBe 5
        polygon.coordinates[1].size shouldBe 5
    }

    test("deserialize Polygon empty not supported") {
        //given
        val wkt = "POLYGON EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize Polygon EWKT") {
        //given
        val wkt = "SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val polygon = geom.shouldBeInstanceOf<Polygon>()

        //then
        polygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    // MultiPoint Tests
    test("deserialize MultiPoint") {
        //given
        val wkt = "MULTIPOINT ((10.0 20.0), (30.0 40.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPoint = geom.shouldBeInstanceOf<MultiPoint>()

        //then
        multiPoint.coordinates.size shouldBe 2
        multiPoint.coordinates[0].longitude shouldBe 10.0
        multiPoint.coordinates[0].latitude shouldBe 20.0
        multiPoint.coordinates[1].longitude shouldBe 30.0
        multiPoint.coordinates[1].latitude shouldBe 40.0
    }

    test("deserialize MultiPoint single") {
        //given
        val wkt = "MULTIPOINT ((10.0 20.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPoint = geom.shouldBeInstanceOf<MultiPoint>()

        //then
        multiPoint.coordinates.size shouldBe 1
    }

    test("deserialize MultiPoint empty not supported") {
        //given
        val wkt = "MULTIPOINT EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize MultiPoint with altitude") {
        //given
        val wkt = "MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPoint = geom.shouldBeInstanceOf<MultiPoint>()

        //then
        multiPoint.coordinates[0].altitude shouldBe 5.0
        multiPoint.coordinates[1].altitude shouldBe 15.0
    }

    test("deserialize MultiPoint EWKT") {
        //given
        val wkt = "SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPoint = geom.shouldBeInstanceOf<MultiPoint>()

        //then
        multiPoint.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    // MultiLineString Tests
    test("deserialize MultiLineString") {
        //given
        val wkt = "MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiLineString = geom.shouldBeInstanceOf<MultiLineString>()

        //then
        multiLineString.coordinates.size shouldBe 2
        multiLineString.coordinates[0].size shouldBe 2
        multiLineString.coordinates[1].size shouldBe 2
        multiLineString.coordinates[0][0].longitude shouldBe 10.0
        multiLineString.coordinates[1][1].longitude shouldBe 70.0
    }

    test("deserialize MultiLineString single") {
        //given
        val wkt = "MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiLineString = geom.shouldBeInstanceOf<MultiLineString>()

        //then
        multiLineString.coordinates.size shouldBe 1
        multiLineString.coordinates[0].size shouldBe 3
    }

    test("deserialize MultiLineString empty not supported") {
        //given
        val wkt = "MULTILINESTRING EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize MultiLineString EWKT") {
        //given
        val wkt = "SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiLineString = geom.shouldBeInstanceOf<MultiLineString>()

        //then
        multiLineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    // MultiPolygon Tests
    test("deserialize MultiPolygon") {
        //given
        val wkt = "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPolygon = geom.shouldBeInstanceOf<MultiPolygon>()

        //then
        multiPolygon.coordinates.size shouldBe 2
        multiPolygon.coordinates[0].size shouldBe 1
        multiPolygon.coordinates[0][0].size shouldBe 5
        multiPolygon.coordinates[0][0][0].longitude shouldBe 0.0
        multiPolygon.coordinates[1][0][0].longitude shouldBe 20.0
    }

    test("deserialize MultiPolygon single") {
        //given
        val wkt = "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPolygon = geom.shouldBeInstanceOf<MultiPolygon>()

        //then
        multiPolygon.coordinates.size shouldBe 1
    }

    test("deserialize MultiPolygon with holes") {
        //given
        val wkt = "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 2.0)))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPolygon = geom.shouldBeInstanceOf<MultiPolygon>()

        //then
        multiPolygon.coordinates.size shouldBe 1
        multiPolygon.coordinates[0].size shouldBe 2
    }

    test("deserialize MultiPolygon empty") {
        //given
        val wkt = "MULTIPOLYGON EMPTY"

        //when
        val action = { WktDeserializer.fromWkt(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize MultiPolygon EWKT") {
        //given
        val wkt = "SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"

        //when
        val geom = WktDeserializer.fromWkt(wkt)
        val multiPolygon = geom.shouldBeInstanceOf<MultiPolygon>()

        //then
        multiPolygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    // GeometryCollection Tests
    test("deserialize GeometryCollection") {
        //given
        val wkt = "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 2
        collection.geometries[0].shouldBeInstanceOf<Point>()
        collection.geometries[1].shouldBeInstanceOf<LineString>()
    }

    test("deserialize GeometryCollection mixed") {
        //given
        val wkt = "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 3
        collection.geometries[0].shouldBeInstanceOf<Point>()
        collection.geometries[1].shouldBeInstanceOf<LineString>()
        collection.geometries[2].shouldBeInstanceOf<Polygon>()
    }

    test("deserialize GeometryCollection empty not supported") {
        //given
        val wkt = "GEOMETRYCOLLECTION EMPTY"

        //when
        val action = { WktDeserializer.fromWktAsCollection(wkt) }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize GeometryCollection with multi geometries") {
        //given
        val wkt = "GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 2
        collection.geometries[0].shouldBeInstanceOf<MultiPoint>()
        collection.geometries[1].shouldBeInstanceOf<MultiLineString>()
    }

    test("deserialize GeometryCollection EWKT") {
        //given
        val wkt = "SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 2
        // Verify that all member geometries have the correct CRS from EWKT SRID
        collection.geometries[0].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
        collection.geometries[1].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("deserialize GeometryCollection EWKT WGS84") {
        //given
        val wkt =
            "SRID=4326;GEOMETRYCOLLECTION (POINT (10.0 20.0), POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 2
        // Verify that all member geometries have WGS_84 CRS
        collection.geometries[0].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
        collection.geometries[1].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("deserialize GeometryCollection EWKT with multi geometries") {
        //given
        val wkt = "SRID=3857;GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                "MULTILINESTRING ((10.0 20.0, 30.0 40.0)), " +
                "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 3
        // Verify that all multi-geometries have the correct CRS from EWKT SRID
        val multiPoint = collection.geometries[0].shouldBeInstanceOf<MultiPoint>()
        multiPoint.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR

        val multiLineString = collection.geometries[1].shouldBeInstanceOf<MultiLineString>()
        multiLineString.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR

        val multiPolygon = collection.geometries[2].shouldBeInstanceOf<MultiPolygon>()
        multiPolygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
    }

    test("deserialize GeometryCollection without SRID") {
        //given
        val wkt = "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"

        //when
        val collection = WktDeserializer.fromWktAsCollection(wkt)

        //then
        collection.geometries.size shouldBe 2
        // Without SRID, should default to WGS_84
        collection.geometries[0].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
        collection.geometries[1].coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WGS_84
    }

    test("round trip Point") {
        //given
        val original = Point(10.5, 20.3, 100.0)

        //when
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWkt(wkt)
        val point = deserialized.shouldBeInstanceOf<Point>()

        //then
        point.coordinates.longitude shouldBe original.coordinates.longitude
        point.coordinates.latitude shouldBe original.coordinates.latitude
        point.coordinates.altitude shouldBe original.coordinates.altitude
    }

    test("round trip LineString") {
        //given
        val original = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0),
                Position(50.0, 60.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWkt(wkt)
        val lineString = deserialized.shouldBeInstanceOf<LineString>()

        //then
        lineString.coordinates.size shouldBe 3
    }

    test("round trip Polygon with EWKT") {
        //given
        val original = Polygon(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 10.0),
                    Position(0.0, 0.0)
                )
            ),
            CoordinateReferenceSystem.WEB_MERCATOR
        )

        //when
        val ewkt = WktSerializer.toEwkt(original)
        val deserialized = WktDeserializer.fromWkt(ewkt)
        val polygon = deserialized.shouldBeInstanceOf<Polygon>()

        //then
        polygon.coordinateReferenceSystem shouldBe CoordinateReferenceSystem.WEB_MERCATOR
        polygon.coordinates.size shouldBe 1
        polygon.coordinates[0].size shouldBe 5
    }

    test("round trip GeometryCollection") {
        //given
        val original = GeometryCollection(
            listOf(
                Point(10.0, 20.0),
                LineString(listOf(Position(30.0, 40.0), Position(50.0, 60.0)))
            )
        )

        //when
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWktAsCollection(wkt)

        //then
        deserialized.geometries.size shouldBe 2
    }

    // Error Handling Tests
    test("deserialize empty WKT") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize blank WKT") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("   ") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize invalid WKT") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("INVALID") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize unsupported SRID") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("SRID=9999;POINT (10.0 20.0)") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize invalid geometry type") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("TRIANGLE (0 0, 1 0, 0 1, 0 0)") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize invalid coordinates") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("POINT (abc def)") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize insufficient coordinates") {
        //given

        //when
        val action = { WktDeserializer.fromWkt("POINT (10.0)") }

        //then
        shouldThrow<WktException>(action)
    }

    test("deserialize wrong collection type") {
        //given

        //when
        val action = { WktDeserializer.fromWktAsCollection("POINT (10.0 20.0)") }

        //then
        shouldThrow<WktException>(action)
    }
})
