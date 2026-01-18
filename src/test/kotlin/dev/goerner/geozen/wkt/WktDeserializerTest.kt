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

internal class WktDeserializerTest {

    // Point Tests
    @Test
    fun testDeserializePoint() {
        val geom = WktDeserializer.fromWkt("POINT (10.5 20.3)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(10.5, point.coordinates.longitude)
        Assertions.assertEquals(20.3, point.coordinates.latitude)
        Assertions.assertEquals(0.0, point.coordinates.altitude)
    }

    @Test
    fun testDeserializePointWithAltitude() {
        val geom = WktDeserializer.fromWkt("POINT (10.5 20.3 100.0)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(10.5, point.coordinates.longitude)
        Assertions.assertEquals(20.3, point.coordinates.latitude)
        Assertions.assertEquals(100.0, point.coordinates.altitude)
    }

    @Test
    fun testDeserializePointCaseInsensitive() {
        val geom = WktDeserializer.fromWkt("point (10.5 20.3)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(10.5, point.coordinates.longitude)
        Assertions.assertEquals(20.3, point.coordinates.latitude)
    }

    @Test
    fun testDeserializePointEwktWgs84() {
        val geom = WktDeserializer.fromWkt("SRID=4326;POINT (10.5 20.3)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(10.5, point.coordinates.longitude)
        Assertions.assertEquals(20.3, point.coordinates.latitude)
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, point.coordinateReferenceSystem)
    }

    @Test
    fun testDeserializePointEwktWebMercator() {
        val geom = WktDeserializer.fromWkt("SRID=3857;POINT (10.5 20.3)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.coordinateReferenceSystem)
    }

    @Test
    fun testDeserializePointEmpty() {
        val geom = WktDeserializer.fromWkt("POINT EMPTY")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(Double.NaN, point.coordinates.longitude)
        Assertions.assertEquals(Double.NaN, point.coordinates.latitude)
    }

    @Test
    fun testDeserializePointNegativeCoordinates() {
        val geom = WktDeserializer.fromWkt("POINT (-122.4194 37.7749)")
        Assertions.assertInstanceOf<Point?>(Point::class.java, geom)
        val point = geom as Point
        Assertions.assertEquals(-122.4194, point.coordinates.longitude)
        Assertions.assertEquals(37.7749, point.coordinates.latitude)
    }

    // LineString Tests
    @Test
    fun testDeserializeLineString() {
        val geom = WktDeserializer.fromWkt("LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)")
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, geom)
        val lineString = geom as LineString
        Assertions.assertEquals(3, lineString.coordinates.size)
        Assertions.assertEquals(10.0, lineString.coordinates[0].longitude)
        Assertions.assertEquals(20.0, lineString.coordinates[0].latitude)
        Assertions.assertEquals(50.0, lineString.coordinates[2].longitude)
        Assertions.assertEquals(60.0, lineString.coordinates[2].latitude)
    }

    @Test
    fun testDeserializeLineStringEmpty() {
        val geom = WktDeserializer.fromWkt("LINESTRING EMPTY")
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, geom)
        val lineString = geom as LineString
        Assertions.assertTrue(lineString.coordinates.isEmpty())
    }

    @Test
    fun testDeserializeLineStringWithAltitude() {
        val geom = WktDeserializer.fromWkt("LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)")
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, geom)
        val lineString = geom as LineString
        Assertions.assertEquals(2, lineString.coordinates.size)
        Assertions.assertEquals(5.0, lineString.coordinates[0].altitude)
        Assertions.assertEquals(10.0, lineString.coordinates[1].altitude)
    }

    @Test
    fun testDeserializeLineStringEwkt() {
        val geom = WktDeserializer.fromWkt("SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)")
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, geom)
        val lineString = geom as LineString
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, lineString.coordinateReferenceSystem)
    }

    // Polygon Tests
    @Test
    fun testDeserializePolygon() {
        val geom = WktDeserializer.fromWkt("POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))")
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, geom)
        val polygon = geom as Polygon
        Assertions.assertEquals(1, polygon.coordinates.size)
        Assertions.assertEquals(5, polygon.coordinates[0].size)
        Assertions.assertEquals(0.0, polygon.coordinates[0][0].longitude)
        Assertions.assertEquals(0.0, polygon.coordinates[0][0].latitude)
    }

    @Test
    fun testDeserializePolygonWithHole() {
        val geom = WktDeserializer.fromWkt(
            "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                    "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))"
        )
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, geom)
        val polygon = geom as Polygon
        Assertions.assertEquals(2, polygon.coordinates.size)
        Assertions.assertEquals(5, polygon.coordinates[0].size)
        Assertions.assertEquals(5, polygon.coordinates[1].size)
    }

    @Test
    fun testDeserializePolygonEmpty() {
        val geom = WktDeserializer.fromWkt("POLYGON EMPTY")
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, geom)
        val polygon = geom as Polygon
        Assertions.assertTrue(polygon.coordinates.isEmpty())
    }

    @Test
    fun testDeserializePolygonEwkt() {
        val geom = WktDeserializer.fromWkt("SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))")
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, geom)
        val polygon = geom as Polygon
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, polygon.coordinateReferenceSystem)
    }

    // MultiPoint Tests
    @Test
    fun testDeserializeMultiPoint() {
        val geom = WktDeserializer.fromWkt("MULTIPOINT ((10.0 20.0), (30.0 40.0))")
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, geom)
        val multiPoint = geom as MultiPoint
        Assertions.assertEquals(2, multiPoint.coordinates.size)
        Assertions.assertEquals(10.0, multiPoint.coordinates[0].longitude)
        Assertions.assertEquals(20.0, multiPoint.coordinates[0].latitude)
        Assertions.assertEquals(30.0, multiPoint.coordinates[1].longitude)
        Assertions.assertEquals(40.0, multiPoint.coordinates[1].latitude)
    }

    @Test
    fun testDeserializeMultiPointSingle() {
        val geom = WktDeserializer.fromWkt("MULTIPOINT ((10.0 20.0))")
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, geom)
        val multiPoint = geom as MultiPoint
        Assertions.assertEquals(1, multiPoint.coordinates.size)
    }

    @Test
    fun testDeserializeMultiPointEmpty() {
        val geom = WktDeserializer.fromWkt("MULTIPOINT EMPTY")
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, geom)
        val multiPoint = geom as MultiPoint
        Assertions.assertTrue(multiPoint.coordinates.isEmpty())
    }

    @Test
    fun testDeserializeMultiPointWithAltitude() {
        val geom = WktDeserializer.fromWkt("MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))")
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, geom)
        val multiPoint = geom as MultiPoint
        Assertions.assertEquals(5.0, multiPoint.coordinates[0].altitude)
        Assertions.assertEquals(15.0, multiPoint.coordinates[1].altitude)
    }

    @Test
    fun testDeserializeMultiPointEwkt() {
        val geom = WktDeserializer.fromWkt("SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))")
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, geom)
        val multiPoint = geom as MultiPoint
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPoint.coordinateReferenceSystem)
    }

    // MultiLineString Tests
    @Test
    fun testDeserializeMultiLineString() {
        val geom = WktDeserializer.fromWkt("MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))")
        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, geom)
        val multiLineString = geom as MultiLineString
        Assertions.assertEquals(2, multiLineString.coordinates.size)
        Assertions.assertEquals(2, multiLineString.coordinates[0].size)
        Assertions.assertEquals(2, multiLineString.coordinates[1].size)
        Assertions.assertEquals(10.0, multiLineString.coordinates[0][0].longitude)
        Assertions.assertEquals(70.0, multiLineString.coordinates[1][1].longitude)
    }

    @Test
    fun testDeserializeMultiLineStringSingle() {
        val geom = WktDeserializer.fromWkt("MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))")
        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, geom)
        val multiLineString = geom as MultiLineString
        Assertions.assertEquals(1, multiLineString.coordinates.size)
        Assertions.assertEquals(3, multiLineString.coordinates[0].size)
    }

    @Test
    fun testDeserializeMultiLineStringEmpty() {
        val geom = WktDeserializer.fromWkt("MULTILINESTRING EMPTY")
        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, geom)
        val multiLineString = geom as MultiLineString
        Assertions.assertTrue(multiLineString.coordinates.isEmpty())
    }

    @Test
    fun testDeserializeMultiLineStringEwkt() {
        val geom = WktDeserializer.fromWkt("SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))")
        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, geom)
        val multiLineString = geom as MultiLineString
        Assertions.assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.coordinateReferenceSystem)
    }

    // MultiPolygon Tests
    @Test
    fun testDeserializeMultiPolygon() {
        val geom = WktDeserializer.fromWkt(
            "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                    "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))"
        )
        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, geom)
        val multiPolygon = geom as MultiPolygon
        Assertions.assertEquals(2, multiPolygon.coordinates.size)
        Assertions.assertEquals(1, multiPolygon.coordinates[0].size)
        Assertions.assertEquals(5, multiPolygon.coordinates[0][0].size)
        Assertions.assertEquals(0.0, multiPolygon.coordinates[0][0][0].longitude)
        Assertions.assertEquals(20.0, multiPolygon.coordinates[1][0][0].longitude)
    }

    @Test
    fun testDeserializeMultiPolygonSingle() {
        val geom = WktDeserializer.fromWkt(
            "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"
        )
        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, geom)
        val multiPolygon = geom as MultiPolygon
        Assertions.assertEquals(1, multiPolygon.coordinates.size)
    }

    @Test
    fun testDeserializeMultiPolygonWithHoles() {
        val geom = WktDeserializer.fromWkt(
            "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                    "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 2.0)))"
        )
        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, geom)
        val multiPolygon = geom as MultiPolygon
        Assertions.assertEquals(1, multiPolygon.coordinates.size)
        Assertions.assertEquals(2, multiPolygon.coordinates[0].size)
    }

    @Test
    fun testDeserializeMultiPolygonEmpty() {
        val geom = WktDeserializer.fromWkt("MULTIPOLYGON EMPTY")
        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, geom)
        val multiPolygon = geom as MultiPolygon
        Assertions.assertTrue(multiPolygon.coordinates.isEmpty())
    }

    @Test
    fun testDeserializeMultiPolygonEwkt() {
        val geom = WktDeserializer.fromWkt("SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))")
        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, geom)
        val multiPolygon = geom as MultiPolygon
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPolygon.coordinateReferenceSystem)
    }

    // GeometryCollection Tests
    @Test
    fun testDeserializeGeometryCollection() {
        val collection = WktDeserializer.fromWktAsCollection(
            "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        )
        Assertions.assertEquals(2, collection.geometries.size)
        Assertions.assertInstanceOf<Point?>(Point::class.java, collection.geometries[0])
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, collection.geometries[1])
    }

    @Test
    fun testDeserializeGeometryCollectionMixed() {
        val collection = WktDeserializer.fromWktAsCollection(
            "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                    "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"
        )
        Assertions.assertEquals(3, collection.geometries.size)
        Assertions.assertInstanceOf<Point?>(Point::class.java, collection.geometries[0])
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, collection.geometries[1])
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, collection.geometries[2])
    }

    @Test
    fun testDeserializeGeometryCollectionEmpty() {
        val collection = WktDeserializer.fromWktAsCollection("GEOMETRYCOLLECTION EMPTY")
        Assertions.assertTrue(collection.geometries.isEmpty())
    }

    @Test
    fun testDeserializeGeometryCollectionWithMultiGeometries() {
        val collection = WktDeserializer.fromWktAsCollection(
            "GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                    "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))"
        )
        Assertions.assertEquals(2, collection.geometries.size)
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, collection.geometries[0])
        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, collection.geometries[1])
    }

    @Test
    fun testDeserializeGeometryCollectionEwkt() {
        val collection = WktDeserializer.fromWktAsCollection(
            "SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        )
        Assertions.assertEquals(2, collection.geometries.size)
        // Verify that all member geometries have the correct CRS from EWKT SRID
        Assertions.assertEquals(
            CoordinateReferenceSystem.WEB_MERCATOR,
            collection.geometries[0].coordinateReferenceSystem
        )
        Assertions.assertEquals(
            CoordinateReferenceSystem.WEB_MERCATOR,
            collection.geometries[1].coordinateReferenceSystem
        )
    }

    @Test
    fun testDeserializeGeometryCollectionEwktWgs84() {
        val collection = WktDeserializer.fromWktAsCollection(
            "SRID=4326;GEOMETRYCOLLECTION (POINT (10.0 20.0), POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"
        )
        Assertions.assertEquals(2, collection.geometries.size)
        // Verify that all member geometries have WGS_84 CRS
        Assertions.assertEquals(
            CoordinateReferenceSystem.WGS_84,
            collection.geometries[0].coordinateReferenceSystem
        )
        Assertions.assertEquals(
            CoordinateReferenceSystem.WGS_84,
            collection.geometries[1].coordinateReferenceSystem
        )
    }

    @Test
    fun testDeserializeGeometryCollectionEwktWithMultiGeometries() {
        val collection = WktDeserializer.fromWktAsCollection(
            "SRID=3857;GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                    "MULTILINESTRING ((10.0 20.0, 30.0 40.0)), " +
                    "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))))"
        )
        Assertions.assertEquals(3, collection.geometries.size)
        // Verify that all multi-geometries have the correct CRS from EWKT SRID
        Assertions.assertInstanceOf<MultiPoint?>(MultiPoint::class.java, collection.geometries[0])
        Assertions.assertEquals(
            CoordinateReferenceSystem.WEB_MERCATOR,
            collection.geometries[0].coordinateReferenceSystem
        )

        Assertions.assertInstanceOf<MultiLineString?>(MultiLineString::class.java, collection.geometries[1])
        Assertions.assertEquals(
            CoordinateReferenceSystem.WEB_MERCATOR,
            collection.geometries[1].coordinateReferenceSystem
        )

        Assertions.assertInstanceOf<MultiPolygon?>(MultiPolygon::class.java, collection.geometries[2])
        Assertions.assertEquals(
            CoordinateReferenceSystem.WEB_MERCATOR,
            collection.geometries[2].coordinateReferenceSystem
        )
    }

    @Test
    fun testDeserializeGeometryCollectionWithoutSrid() {
        val collection = WktDeserializer.fromWktAsCollection(
            "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        )
        Assertions.assertEquals(2, collection.geometries.size)
        // Without SRID, should default to WGS_84
        Assertions.assertEquals(
            CoordinateReferenceSystem.WGS_84,
            collection.geometries[0].coordinateReferenceSystem
        )
        Assertions.assertEquals(
            CoordinateReferenceSystem.WGS_84,
            collection.geometries[1].coordinateReferenceSystem
        )
    }

    @Test
    fun testRoundTripPoint() {
        val original = Point(10.5, 20.3, 100.0)
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWkt(wkt)
        Assertions.assertInstanceOf<Point?>(Point::class.java, deserialized)
        val point = deserialized as Point
        Assertions.assertEquals(original.coordinates.longitude, point.coordinates.longitude)
        Assertions.assertEquals(original.coordinates.latitude, point.coordinates.latitude)
        Assertions.assertEquals(original.coordinates.altitude, point.coordinates.altitude)
    }

    @Test
    fun testRoundTripLineString() {
        val original = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0),
                Position(50.0, 60.0)
            )
        )
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWkt(wkt)
        Assertions.assertInstanceOf<LineString?>(LineString::class.java, deserialized)
        val lineString = deserialized as LineString
        Assertions.assertEquals(3, lineString.coordinates.size)
    }

    @Test
    fun testRoundTripPolygonWithEwkt() {
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
        val ewkt = WktSerializer.toEwkt(original)
        val deserialized = WktDeserializer.fromWkt(ewkt)
        Assertions.assertInstanceOf<Polygon?>(Polygon::class.java, deserialized)
        val polygon = deserialized as Polygon
        Assertions.assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.coordinateReferenceSystem)
        Assertions.assertEquals(1, polygon.coordinates.size)
        Assertions.assertEquals(5, polygon.coordinates[0].size)
    }

    @Test
    fun testRoundTripGeometryCollection() {
        val original = GeometryCollection(
            listOf(
                Point(10.0, 20.0),
                LineString(listOf(Position(30.0, 40.0), Position(50.0, 60.0)))
            )
        )
        val wkt = WktSerializer.toWkt(original)
        val deserialized = WktDeserializer.fromWktAsCollection(wkt)
        Assertions.assertEquals(2, deserialized.geometries.size)
    }

    // Error Handling Tests
    @Test
    fun testDeserializeEmptyWkt() {
        Assertions.assertThrows<WktException?>(WktException::class.java) { WktDeserializer.fromWkt("") }
    }

    @Test
    fun testDeserializeBlankWkt() {
        Assertions.assertThrows<WktException?>(WktException::class.java) { WktDeserializer.fromWkt("   ") }
    }

    @Test
    fun testDeserializeInvalidWkt() {
        Assertions.assertThrows<WktException?>(WktException::class.java) { WktDeserializer.fromWkt("INVALID") }
    }

    @Test
    fun testDeserializeUnsupportedSrid() {
        Assertions.assertThrows<WktException?>(
            WktException::class.java
        ) { WktDeserializer.fromWkt("SRID=9999;POINT (10.0 20.0)") }
    }

    @Test
    fun testDeserializeInvalidGeometryType() {
        Assertions.assertThrows<WktException?>(
            WktException::class.java
        ) { WktDeserializer.fromWkt("TRIANGLE (0 0, 1 0, 0 1, 0 0)") }
    }

    @Test
    fun testDeserializeInvalidCoordinates() {
        Assertions.assertThrows<WktException?>(
            WktException::class.java
        ) { WktDeserializer.fromWkt("POINT (abc def)") }
    }

    @Test
    fun testDeserializeInsufficientCoordinates() {
        Assertions.assertThrows<WktException?>(
            WktException::class.java
        ) { WktDeserializer.fromWkt("POINT (10.0)") }
    }

    @Test
    fun testDeserializeWrongCollectionType() {
        Assertions.assertThrows<WktException?>(
            WktException::class.java
        ) { WktDeserializer.fromWktAsCollection("POINT (10.0 20.0)") }
    }
}
