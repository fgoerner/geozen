package dev.goerner.geozen.wkt;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WktDeserializerTest {

    private final WktDeserializer deserializer = new WktDeserializer();

    // Point Tests
    @Test
    void testDeserializePoint() {
        Geometry geom = deserializer.fromWkt("POINT (10.5 20.3)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(10.5, point.getCoordinates().getLongitude());
        assertEquals(20.3, point.getCoordinates().getLatitude());
        assertEquals(0.0, point.getCoordinates().getAltitude());
    }

    @Test
    void testDeserializePointWithAltitude() {
        Geometry geom = deserializer.fromWkt("POINT (10.5 20.3 100.0)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(10.5, point.getCoordinates().getLongitude());
        assertEquals(20.3, point.getCoordinates().getLatitude());
        assertEquals(100.0, point.getCoordinates().getAltitude());
    }

    @Test
    void testDeserializePointCaseInsensitive() {
        Geometry geom = deserializer.fromWkt("point (10.5 20.3)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(10.5, point.getCoordinates().getLongitude());
        assertEquals(20.3, point.getCoordinates().getLatitude());
    }

    @Test
    void testDeserializePointEwktWgs84() {
        Geometry geom = deserializer.fromWkt("SRID=4326;POINT (10.5 20.3)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(10.5, point.getCoordinates().getLongitude());
        assertEquals(20.3, point.getCoordinates().getLatitude());
        assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
    }

    @Test
    void testDeserializePointEwktWebMercator() {
        Geometry geom = deserializer.fromWkt("SRID=3857;POINT (10.5 20.3)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
    }

    @Test
    void testDeserializePointEmpty() {
        Geometry geom = deserializer.fromWkt("POINT EMPTY");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(Double.NaN, point.getCoordinates().getLongitude());
        assertEquals(Double.NaN, point.getCoordinates().getLatitude());
    }

    @Test
    void testDeserializePointNegativeCoordinates() {
        Geometry geom = deserializer.fromWkt("POINT (-122.4194 37.7749)");
        assertInstanceOf(Point.class, geom);
        Point point = (Point) geom;
        assertEquals(-122.4194, point.getCoordinates().getLongitude());
        assertEquals(37.7749, point.getCoordinates().getLatitude());
    }

    // LineString Tests
    @Test
    void testDeserializeLineString() {
        Geometry geom = deserializer.fromWkt("LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)");
        assertInstanceOf(LineString.class, geom);
        LineString lineString = (LineString) geom;
        assertEquals(3, lineString.getCoordinates().size());
        assertEquals(10.0, lineString.getCoordinates().getFirst().getLongitude());
        assertEquals(20.0, lineString.getCoordinates().getFirst().getLatitude());
        assertEquals(50.0, lineString.getCoordinates().get(2).getLongitude());
        assertEquals(60.0, lineString.getCoordinates().get(2).getLatitude());
    }

    @Test
    void testDeserializeLineStringEmpty() {
        Geometry geom = deserializer.fromWkt("LINESTRING EMPTY");
        assertInstanceOf(LineString.class, geom);
        LineString lineString = (LineString) geom;
        assertTrue(lineString.getCoordinates().isEmpty());
    }

    @Test
    void testDeserializeLineStringWithAltitude() {
        Geometry geom = deserializer.fromWkt("LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)");
        assertInstanceOf(LineString.class, geom);
        LineString lineString = (LineString) geom;
        assertEquals(2, lineString.getCoordinates().size());
        assertEquals(5.0, lineString.getCoordinates().getFirst().getAltitude());
        assertEquals(10.0, lineString.getCoordinates().get(1).getAltitude());
    }

    @Test
    void testDeserializeLineStringEwkt() {
        Geometry geom = deserializer.fromWkt("SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)");
        assertInstanceOf(LineString.class, geom);
        LineString lineString = (LineString) geom;
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, lineString.getCoordinateReferenceSystem());
    }

    // Polygon Tests
    @Test
    void testDeserializePolygon() {
        Geometry geom = deserializer.fromWkt("POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))");
        assertInstanceOf(Polygon.class, geom);
        Polygon polygon = (Polygon) geom;
        assertEquals(1, polygon.getCoordinates().size());
        assertEquals(5, polygon.getCoordinates().getFirst().size());
        assertEquals(0.0, polygon.getCoordinates().getFirst().getFirst().getLongitude());
        assertEquals(0.0, polygon.getCoordinates().getFirst().getFirst().getLatitude());
    }

    @Test
    void testDeserializePolygonWithHole() {
        Geometry geom = deserializer.fromWkt(
                "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                        "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))"
        );
        assertInstanceOf(Polygon.class, geom);
        Polygon polygon = (Polygon) geom;
        assertEquals(2, polygon.getCoordinates().size());
        assertEquals(5, polygon.getCoordinates().getFirst().size());
        assertEquals(5, polygon.getCoordinates().get(1).size());
    }

    @Test
    void testDeserializePolygonEmpty() {
        Geometry geom = deserializer.fromWkt("POLYGON EMPTY");
        assertInstanceOf(Polygon.class, geom);
        Polygon polygon = (Polygon) geom;
        assertTrue(polygon.getCoordinates().isEmpty());
    }

    @Test
    void testDeserializePolygonEwkt() {
        Geometry geom = deserializer.fromWkt("SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))");
        assertInstanceOf(Polygon.class, geom);
        Polygon polygon = (Polygon) geom;
        assertEquals(CoordinateReferenceSystem.WGS_84, polygon.getCoordinateReferenceSystem());
    }

    // MultiPoint Tests
    @Test
    void testDeserializeMultiPoint() {
        Geometry geom = deserializer.fromWkt("MULTIPOINT ((10.0 20.0), (30.0 40.0))");
        assertInstanceOf(MultiPoint.class, geom);
        MultiPoint multiPoint = (MultiPoint) geom;
        assertEquals(2, multiPoint.getCoordinates().size());
        assertEquals(10.0, multiPoint.getCoordinates().getFirst().getLongitude());
        assertEquals(20.0, multiPoint.getCoordinates().getFirst().getLatitude());
        assertEquals(30.0, multiPoint.getCoordinates().get(1).getLongitude());
        assertEquals(40.0, multiPoint.getCoordinates().get(1).getLatitude());
    }

    @Test
    void testDeserializeMultiPointSingle() {
        Geometry geom = deserializer.fromWkt("MULTIPOINT ((10.0 20.0))");
        assertInstanceOf(MultiPoint.class, geom);
        MultiPoint multiPoint = (MultiPoint) geom;
        assertEquals(1, multiPoint.getCoordinates().size());
    }

    @Test
    void testDeserializeMultiPointEmpty() {
        Geometry geom = deserializer.fromWkt("MULTIPOINT EMPTY");
        assertInstanceOf(MultiPoint.class, geom);
        MultiPoint multiPoint = (MultiPoint) geom;
        assertTrue(multiPoint.getCoordinates().isEmpty());
    }

    @Test
    void testDeserializeMultiPointWithAltitude() {
        Geometry geom = deserializer.fromWkt("MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))");
        assertInstanceOf(MultiPoint.class, geom);
        MultiPoint multiPoint = (MultiPoint) geom;
        assertEquals(5.0, multiPoint.getCoordinates().getFirst().getAltitude());
        assertEquals(15.0, multiPoint.getCoordinates().get(1).getAltitude());
    }

    @Test
    void testDeserializeMultiPointEwkt() {
        Geometry geom = deserializer.fromWkt("SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))");
        assertInstanceOf(MultiPoint.class, geom);
        MultiPoint multiPoint = (MultiPoint) geom;
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPoint.getCoordinateReferenceSystem());
    }

    // MultiLineString Tests
    @Test
    void testDeserializeMultiLineString() {
        Geometry geom = deserializer.fromWkt("MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))");
        assertInstanceOf(MultiLineString.class, geom);
        MultiLineString multiLineString = (MultiLineString) geom;
        assertEquals(2, multiLineString.getCoordinates().size());
        assertEquals(2, multiLineString.getCoordinates().getFirst().size());
        assertEquals(2, multiLineString.getCoordinates().get(1).size());
        assertEquals(10.0, multiLineString.getCoordinates().getFirst().getFirst().getLongitude());
        assertEquals(70.0, multiLineString.getCoordinates().get(1).get(1).getLongitude());
    }

    @Test
    void testDeserializeMultiLineStringSingle() {
        Geometry geom = deserializer.fromWkt("MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))");
        assertInstanceOf(MultiLineString.class, geom);
        MultiLineString multiLineString = (MultiLineString) geom;
        assertEquals(1, multiLineString.getCoordinates().size());
        assertEquals(3, multiLineString.getCoordinates().getFirst().size());
    }

    @Test
    void testDeserializeMultiLineStringEmpty() {
        Geometry geom = deserializer.fromWkt("MULTILINESTRING EMPTY");
        assertInstanceOf(MultiLineString.class, geom);
        MultiLineString multiLineString = (MultiLineString) geom;
        assertTrue(multiLineString.getCoordinates().isEmpty());
    }

    @Test
    void testDeserializeMultiLineStringEwkt() {
        Geometry geom = deserializer.fromWkt("SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))");
        assertInstanceOf(MultiLineString.class, geom);
        MultiLineString multiLineString = (MultiLineString) geom;
        assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.getCoordinateReferenceSystem());
    }

    // MultiPolygon Tests
    @Test
    void testDeserializeMultiPolygon() {
        Geometry geom = deserializer.fromWkt(
                "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                        "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))"
        );
        assertInstanceOf(MultiPolygon.class, geom);
        MultiPolygon multiPolygon = (MultiPolygon) geom;
        assertEquals(2, multiPolygon.getCoordinates().size());
        assertEquals(1, multiPolygon.getCoordinates().getFirst().size());
        assertEquals(5, multiPolygon.getCoordinates().getFirst().getFirst().size());
        assertEquals(0.0, multiPolygon.getCoordinates().getFirst().getFirst().getFirst().getLongitude());
        assertEquals(20.0, multiPolygon.getCoordinates().get(1).getFirst().getFirst().getLongitude());
    }

    @Test
    void testDeserializeMultiPolygonSingle() {
        Geometry geom = deserializer.fromWkt(
                "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"
        );
        assertInstanceOf(MultiPolygon.class, geom);
        MultiPolygon multiPolygon = (MultiPolygon) geom;
        assertEquals(1, multiPolygon.getCoordinates().size());
    }

    @Test
    void testDeserializeMultiPolygonWithHoles() {
        Geometry geom = deserializer.fromWkt(
                "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                        "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 2.0)))"
        );
        assertInstanceOf(MultiPolygon.class, geom);
        MultiPolygon multiPolygon = (MultiPolygon) geom;
        assertEquals(1, multiPolygon.getCoordinates().size());
        assertEquals(2, multiPolygon.getCoordinates().getFirst().size());
    }

    @Test
    void testDeserializeMultiPolygonEmpty() {
        Geometry geom = deserializer.fromWkt("MULTIPOLYGON EMPTY");
        assertInstanceOf(MultiPolygon.class, geom);
        MultiPolygon multiPolygon = (MultiPolygon) geom;
        assertTrue(multiPolygon.getCoordinates().isEmpty());
    }

    @Test
    void testDeserializeMultiPolygonEwkt() {
        Geometry geom = deserializer.fromWkt("SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))");
        assertInstanceOf(MultiPolygon.class, geom);
        MultiPolygon multiPolygon = (MultiPolygon) geom;
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPolygon.getCoordinateReferenceSystem());
    }

    // GeometryCollection Tests
    @Test
    void testDeserializeGeometryCollection() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        );
        assertEquals(2, collection.getGeometries().size());
        assertInstanceOf(Point.class, collection.getGeometries().getFirst());
        assertInstanceOf(LineString.class, collection.getGeometries().get(1));
    }

    @Test
    void testDeserializeGeometryCollectionMixed() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                        "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"
        );
        assertEquals(3, collection.getGeometries().size());
        assertInstanceOf(Point.class, collection.getGeometries().getFirst());
        assertInstanceOf(LineString.class, collection.getGeometries().get(1));
        assertInstanceOf(Polygon.class, collection.getGeometries().get(2));
    }

    @Test
    void testDeserializeGeometryCollectionEmpty() {
        GeometryCollection collection = deserializer.fromWktAsCollection("GEOMETRYCOLLECTION EMPTY");
        assertTrue(collection.getGeometries().isEmpty());
    }

    @Test
    void testDeserializeGeometryCollectionWithMultiGeometries() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                        "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))"
        );
        assertEquals(2, collection.getGeometries().size());
        assertInstanceOf(MultiPoint.class, collection.getGeometries().getFirst());
        assertInstanceOf(MultiLineString.class, collection.getGeometries().get(1));
    }

    @Test
    void testDeserializeGeometryCollectionEwkt() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        );
        assertEquals(2, collection.getGeometries().size());
        // Verify that all member geometries have the correct CRS from EWKT SRID
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, collection.getGeometries().getFirst().getCoordinateReferenceSystem());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, collection.getGeometries().get(1).getCoordinateReferenceSystem());
    }

    @Test
    void testDeserializeGeometryCollectionEwktWgs84() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "SRID=4326;GEOMETRYCOLLECTION (POINT (10.0 20.0), POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"
        );
        assertEquals(2, collection.getGeometries().size());
        // Verify that all member geometries have WGS_84 CRS
        assertEquals(CoordinateReferenceSystem.WGS_84, collection.getGeometries().getFirst().getCoordinateReferenceSystem());
        assertEquals(CoordinateReferenceSystem.WGS_84, collection.getGeometries().get(1).getCoordinateReferenceSystem());
    }

    @Test
    void testDeserializeGeometryCollectionEwktWithMultiGeometries() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "SRID=3857;GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                        "MULTILINESTRING ((10.0 20.0, 30.0 40.0)), " +
                        "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))))"
        );
        assertEquals(3, collection.getGeometries().size());
        // Verify that all multi-geometries have the correct CRS from EWKT SRID
        assertInstanceOf(MultiPoint.class, collection.getGeometries().getFirst());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, collection.getGeometries().getFirst().getCoordinateReferenceSystem());

        assertInstanceOf(MultiLineString.class, collection.getGeometries().get(1));
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, collection.getGeometries().get(1).getCoordinateReferenceSystem());

        assertInstanceOf(MultiPolygon.class, collection.getGeometries().get(2));
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, collection.getGeometries().get(2).getCoordinateReferenceSystem());
    }

    @Test
    void testDeserializeGeometryCollectionWithoutSrid() {
        GeometryCollection collection = deserializer.fromWktAsCollection(
                "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
        );
        assertEquals(2, collection.getGeometries().size());
        // Without SRID, should default to WGS_84
        assertEquals(CoordinateReferenceSystem.WGS_84, collection.getGeometries().getFirst().getCoordinateReferenceSystem());
        assertEquals(CoordinateReferenceSystem.WGS_84, collection.getGeometries().get(1).getCoordinateReferenceSystem());
    }

    @Test
    void testRoundTripPoint() {
        WktSerializer serializer = new WktSerializer();
        Point original = new Point(10.5, 20.3, 100.0);
        String wkt = serializer.toWkt(original);
        Geometry deserialized = deserializer.fromWkt(wkt);
        assertInstanceOf(Point.class, deserialized);
        Point point = (Point) deserialized;
        assertEquals(original.getCoordinates().getLongitude(), point.getCoordinates().getLongitude());
        assertEquals(original.getCoordinates().getLatitude(), point.getCoordinates().getLatitude());
        assertEquals(original.getCoordinates().getAltitude(), point.getCoordinates().getAltitude());
    }

    @Test
    void testRoundTripLineString() {
        WktSerializer serializer = new WktSerializer();
        LineString original = new LineString(java.util.List.of(
                new Position(10.0, 20.0),
                new Position(30.0, 40.0),
                new Position(50.0, 60.0)
        ));
        String wkt = serializer.toWkt(original);
        Geometry deserialized = deserializer.fromWkt(wkt);
        assertInstanceOf(LineString.class, deserialized);
        LineString lineString = (LineString) deserialized;
        assertEquals(3, lineString.getCoordinates().size());
    }

    @Test
    void testRoundTripPolygonWithEwkt() {
        WktSerializer serializer = new WktSerializer();
        Polygon original = new Polygon(
                java.util.List.of(
                        java.util.List.of(
                                new Position(0.0, 0.0),
                                new Position(10.0, 0.0),
                                new Position(10.0, 10.0),
                                new Position(0.0, 10.0),
                                new Position(0.0, 0.0)
                        )
                ),
                CoordinateReferenceSystem.WEB_MERCATOR
        );
        String ewkt = serializer.toEwkt(original);
        Geometry deserialized = deserializer.fromWkt(ewkt);
        assertInstanceOf(Polygon.class, deserialized);
        Polygon polygon = (Polygon) deserialized;
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.getCoordinateReferenceSystem());
        assertEquals(1, polygon.getCoordinates().size());
        assertEquals(5, polygon.getCoordinates().getFirst().size());
    }

    @Test
    void testRoundTripGeometryCollection() {
        WktSerializer serializer = new WktSerializer();
        GeometryCollection original = new GeometryCollection(java.util.List.of(
                new Point(10.0, 20.0),
                new LineString(java.util.List.of(new Position(30.0, 40.0), new Position(50.0, 60.0)))
        ));
        String wkt = serializer.toWkt(original);
        GeometryCollection deserialized = deserializer.fromWktAsCollection(wkt);
        assertEquals(2, deserialized.getGeometries().size());
    }

    // Error Handling Tests
    @Test
    void testDeserializeEmptyWkt() {
        assertThrows(WktException.class, () -> deserializer.fromWkt(""));
    }

    @Test
    void testDeserializeBlankWkt() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("   "));
    }

    @Test
    void testDeserializeInvalidWkt() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("INVALID"));
    }

    @Test
    void testDeserializeUnsupportedSrid() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("SRID=9999;POINT (10.0 20.0)"));
    }

    @Test
    void testDeserializeInvalidGeometryType() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("TRIANGLE (0 0, 1 0, 0 1, 0 0)"));
    }

    @Test
    void testDeserializeInvalidCoordinates() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("POINT (abc def)"));
    }

    @Test
    void testDeserializeInsufficientCoordinates() {
        assertThrows(WktException.class, () -> deserializer.fromWkt("POINT (10.0)"));
    }

    @Test
    void testDeserializeWrongCollectionType() {
        assertThrows(WktException.class, () -> deserializer.fromWktAsCollection("POINT (10.0 20.0)"));
    }
}
