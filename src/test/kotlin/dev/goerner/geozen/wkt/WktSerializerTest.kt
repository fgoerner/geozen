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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class WktSerializerTest {

    // Point Tests
    @Test
    fun testSerializePoint() {
        val point = Point(10.5, 20.3)
        val wkt = WktSerializer.toWkt(point)
        Assertions.assertEquals("POINT (10.5 20.3)", wkt)
    }

    @Test
    fun testSerializePointWithAltitude() {
        val point = Point(10.5, 20.3, 100.0)
        val wkt = WktSerializer.toWkt(point)
        Assertions.assertEquals("POINT (10.5 20.3 100.0)", wkt)
    }

    @Test
    fun testSerializePointEwktWgs84() {
        val point = Point(10.5, 20.3)
        val ewkt = WktSerializer.toEwkt(point)
        Assertions.assertEquals("SRID=4326;POINT (10.5 20.3)", ewkt)
    }

    @Test
    fun testSerializePointEwktWebMercator() {
        val point = Point(Position(10.5, 20.3), CoordinateReferenceSystem.WEB_MERCATOR)
        val ewkt = WktSerializer.toEwkt(point)
        Assertions.assertEquals("SRID=3857;POINT (10.5 20.3)", ewkt)
    }

    @Test
    fun testSerializePointNegativeCoordinates() {
        val point = Point(-122.4194, 37.7749)
        val wkt = WktSerializer.toWkt(point)
        Assertions.assertEquals("POINT (-122.4194 37.7749)", wkt)
    }

    // LineString Tests
    @Test
    fun testSerializeLineString() {
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0),
                Position(50.0, 60.0)
            )
        )
        val wkt = WktSerializer.toWkt(lineString)
        Assertions.assertEquals("LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)", wkt)
    }

    @Test
    fun testSerializeLineStringEmpty() {
        val lineString = LineString(emptyList())
        val wkt = WktSerializer.toWkt(lineString)
        Assertions.assertEquals("LINESTRING EMPTY", wkt)
    }

    @Test
    fun testSerializeLineStringWithAltitude() {
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0, 5.0),
                Position(30.0, 40.0, 10.0)
            )
        )
        val wkt = WktSerializer.toWkt(lineString)
        Assertions.assertEquals("LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)", wkt)
    }

    @Test
    fun testSerializeLineStringEwkt() {
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )
        val ewkt = WktSerializer.toEwkt(lineString)
        Assertions.assertEquals("SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)", ewkt)
    }

    // Polygon Tests
    @Test
    fun testSerializePolygon() {
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 10.0),
                    Position(0.0, 0.0)
                )
            )
        )
        val wkt = WktSerializer.toWkt(polygon)
        Assertions.assertEquals("POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))", wkt)
    }

    @Test
    fun testSerializePolygonWithHole() {
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 10.0),
                    Position(0.0, 0.0)
                ),
                listOf(
                    Position(2.0, 2.0),
                    Position(8.0, 2.0),
                    Position(8.0, 8.0),
                    Position(2.0, 8.0),
                    Position(2.0, 2.0)
                )
            )
        )
        val wkt = WktSerializer.toWkt(polygon)
        Assertions.assertEquals(
            "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                    "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))", wkt
        )
    }

    @Test
    fun testSerializePolygonEmpty() {
        val polygon = Polygon(emptyList())
        val wkt = WktSerializer.toWkt(polygon)
        Assertions.assertEquals("POLYGON EMPTY", wkt)
    }

    @Test
    fun testSerializePolygonEwkt() {
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(0.0, 0.0),
                    Position(10.0, 0.0),
                    Position(10.0, 10.0),
                    Position(0.0, 0.0)
                )
            ), CoordinateReferenceSystem.WGS_84
        )
        val ewkt = WktSerializer.toEwkt(polygon)
        Assertions.assertEquals("SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))", ewkt)
    }

    // MultiPoint Tests
    @Test
    fun testSerializeMultiPoint() {
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            )
        )
        val wkt = WktSerializer.toWkt(multiPoint)
        Assertions.assertEquals("MULTIPOINT ((10.0 20.0), (30.0 40.0))", wkt)
    }

    @Test
    fun testSerializeMultiPointSingle() {
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0)
            )
        )
        val wkt = WktSerializer.toWkt(multiPoint)
        Assertions.assertEquals("MULTIPOINT ((10.0 20.0))", wkt)
    }

    @Test
    fun testSerializeMultiPointEmpty() {
        val multiPoint = MultiPoint(emptyList())
        val wkt = WktSerializer.toWkt(multiPoint)
        Assertions.assertEquals("MULTIPOINT EMPTY", wkt)
    }

    @Test
    fun testSerializeMultiPointWithAltitude() {
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0, 5.0),
                Position(30.0, 40.0, 15.0)
            )
        )
        val wkt = WktSerializer.toWkt(multiPoint)
        Assertions.assertEquals("MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))", wkt)
    }

    @Test
    fun testSerializeMultiPointEwkt() {
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )
        val ewkt = WktSerializer.toEwkt(multiPoint)
        Assertions.assertEquals("SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))", ewkt)
    }

    // MultiLineString Tests
    @Test
    fun testSerializeMultiLineString() {
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0)),
                listOf(Position(50.0, 60.0), Position(70.0, 80.0))
            )
        )
        val wkt = WktSerializer.toWkt(multiLineString)
        Assertions.assertEquals("MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))", wkt)
    }

    @Test
    fun testSerializeMultiLineStringSingle() {
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0), Position(50.0, 60.0))
            )
        )
        val wkt = WktSerializer.toWkt(multiLineString)
        Assertions.assertEquals("MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))", wkt)
    }

    @Test
    fun testSerializeMultiLineStringEmpty() {
        val multiLineString = MultiLineString(emptyList())
        val wkt = WktSerializer.toWkt(multiLineString)
        Assertions.assertEquals("MULTILINESTRING EMPTY", wkt)
    }

    @Test
    fun testSerializeMultiLineStringEwkt() {
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0))
            ), CoordinateReferenceSystem.WGS_84
        )
        val ewkt = WktSerializer.toEwkt(multiLineString)
        Assertions.assertEquals("SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))", ewkt)
    }

    // MultiPolygon Tests
    @Test
    fun testSerializeMultiPolygon() {
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(10.0, 0.0),
                        Position(10.0, 10.0),
                        Position(0.0, 10.0),
                        Position(0.0, 0.0)
                    )
                ),
                listOf(
                    listOf(
                        Position(20.0, 20.0),
                        Position(30.0, 20.0),
                        Position(30.0, 30.0),
                        Position(20.0, 30.0),
                        Position(20.0, 20.0)
                    )
                )
            )
        )
        val wkt = WktSerializer.toWkt(multiPolygon)
        Assertions.assertEquals(
            "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                    "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))", wkt
        )
    }

    @Test
    fun testSerializeMultiPolygonSingle() {
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(10.0, 0.0),
                        Position(10.0, 10.0),
                        Position(0.0, 0.0)
                    )
                )
            )
        )
        val wkt = WktSerializer.toWkt(multiPolygon)
        Assertions.assertEquals("MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))", wkt)
    }

    @Test
    fun testSerializeMultiPolygonWithHoles() {
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(10.0, 0.0),
                        Position(10.0, 10.0),
                        Position(0.0, 10.0),
                        Position(0.0, 0.0)
                    ),
                    listOf(
                        Position(2.0, 2.0),
                        Position(8.0, 2.0),
                        Position(8.0, 8.0),
                        Position(2.0, 2.0)
                    )
                )
            )
        )
        val wkt = WktSerializer.toWkt(multiPolygon)
        Assertions.assertEquals(
            "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                    "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 2.0)))", wkt
        )
    }

    @Test
    fun testSerializeMultiPolygonEmpty() {
        val multiPolygon = MultiPolygon(emptyList())
        val wkt = WktSerializer.toWkt(multiPolygon)
        Assertions.assertEquals("MULTIPOLYGON EMPTY", wkt)
    }

    @Test
    fun testSerializeMultiPolygonEwkt() {
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(0.0, 0.0),
                        Position(10.0, 0.0),
                        Position(10.0, 10.0),
                        Position(0.0, 0.0)
                    )
                )
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )
        val ewkt = WktSerializer.toEwkt(multiPolygon)
        Assertions.assertEquals("SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))", ewkt)
    }

    // GeometryCollection Tests
    @Test
    fun testSerializeGeometryCollection() {
        val collection = GeometryCollection(
            listOf(
                Point(10.0, 20.0),
                LineString(listOf(Position(30.0, 40.0), Position(50.0, 60.0)))
            )
        )
        val wkt = WktSerializer.toWkt(collection)
        Assertions.assertEquals("GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))", wkt)
    }

    @Test
    fun testSerializeGeometryCollectionMixed() {
        val collection = GeometryCollection(
            listOf(
                Point(10.0, 20.0),
                LineString(listOf(Position(30.0, 40.0), Position(50.0, 60.0))),
                Polygon(
                    listOf(
                        listOf(
                            Position(0.0, 0.0),
                            Position(5.0, 0.0),
                            Position(5.0, 5.0),
                            Position(0.0, 5.0),
                            Position(0.0, 0.0)
                        )
                    )
                )
            )
        )
        val wkt = WktSerializer.toWkt(collection)
        Assertions.assertEquals(
            "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                    "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))", wkt
        )
    }

    @Test
    fun testSerializeGeometryCollectionEmpty() {
        val collection = GeometryCollection(emptyList())
        val wkt = WktSerializer.toWkt(collection)
        Assertions.assertEquals("GEOMETRYCOLLECTION EMPTY", wkt)
    }

    @Test
    fun testSerializeGeometryCollectionEwkt() {
        val collection = GeometryCollection(
            listOf(
                Point(10.0, 20.0, 0.0, CoordinateReferenceSystem.WEB_MERCATOR),
                LineString(
                    listOf(Position(30.0, 40.0), Position(50.0, 60.0)),
                    CoordinateReferenceSystem.WEB_MERCATOR
                )
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )
        val ewkt = WktSerializer.toEwkt(collection)
        Assertions.assertEquals(
            "SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))",
            ewkt
        )
    }

    @Test
    fun testSerializeGeometryCollectionWithMultiGeometries() {
        val collection = GeometryCollection(
            listOf(
                MultiPoint(listOf(Position(1.0, 2.0), Position(3.0, 4.0))),
                MultiLineString(
                    listOf(
                        listOf(Position(10.0, 20.0), Position(30.0, 40.0))
                    )
                )
            )
        )
        val wkt = WktSerializer.toWkt(collection)
        Assertions.assertEquals(
            "GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                    "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))", wkt
        )
    }
}

