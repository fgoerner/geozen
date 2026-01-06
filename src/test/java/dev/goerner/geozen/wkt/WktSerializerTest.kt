package dev.goerner.geozen.wkt;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WktSerializerTest {

    private final WktSerializer serializer = new WktSerializer();

    // Point Tests
    @Test
    void testSerializePoint() {
        Point point = new Point(10.5, 20.3);
        String wkt = serializer.toWkt(point);
        assertEquals("POINT (10.5 20.3)", wkt);
    }

    @Test
    void testSerializePointWithAltitude() {
        Point point = new Point(10.5, 20.3, 100.0);
        String wkt = serializer.toWkt(point);
        assertEquals("POINT (10.5 20.3 100.0)", wkt);
    }

    @Test
    void testSerializePointEwktWgs84() {
        Point point = new Point(10.5, 20.3);
        String ewkt = serializer.toEwkt(point);
        assertEquals("SRID=4326;POINT (10.5 20.3)", ewkt);
    }

    @Test
    void testSerializePointEwktWebMercator() {
        Point point = new Point(new Position(10.5, 20.3), CoordinateReferenceSystem.WEB_MERCATOR);
        String ewkt = serializer.toEwkt(point);
        assertEquals("SRID=3857;POINT (10.5 20.3)", ewkt);
    }

    @Test
    void testSerializePointNegativeCoordinates() {
        Point point = new Point(-122.4194, 37.7749);
        String wkt = serializer.toWkt(point);
        assertEquals("POINT (-122.4194 37.7749)", wkt);
    }

    // LineString Tests
    @Test
    void testSerializeLineString() {
        LineString lineString = new LineString(List.of(
                new Position(10.0, 20.0),
                new Position(30.0, 40.0),
                new Position(50.0, 60.0)
        ));
        String wkt = serializer.toWkt(lineString);
        assertEquals("LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)", wkt);
    }

    @Test
    void testSerializeLineStringEmpty() {
        LineString lineString = new LineString(List.of());
        String wkt = serializer.toWkt(lineString);
        assertEquals("LINESTRING EMPTY", wkt);
    }

    @Test
    void testSerializeLineStringWithAltitude() {
        LineString lineString = new LineString(List.of(
                new Position(10.0, 20.0, 5.0),
                new Position(30.0, 40.0, 10.0)
        ));
        String wkt = serializer.toWkt(lineString);
        assertEquals("LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)", wkt);
    }

    @Test
    void testSerializeLineStringEwkt() {
        LineString lineString = new LineString(List.of(
                new Position(10.0, 20.0),
                new Position(30.0, 40.0)
        ), CoordinateReferenceSystem.WEB_MERCATOR);
        String ewkt = serializer.toEwkt(lineString);
        assertEquals("SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)", ewkt);
    }

    // Polygon Tests
    @Test
    void testSerializePolygon() {
        Polygon polygon = new Polygon(List.of(
                List.of(
                        new Position(0.0, 0.0),
                        new Position(10.0, 0.0),
                        new Position(10.0, 10.0),
                        new Position(0.0, 10.0),
                        new Position(0.0, 0.0)
                )
        ));
        String wkt = serializer.toWkt(polygon);
        assertEquals("POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))", wkt);
    }

    @Test
    void testSerializePolygonWithHole() {
        Polygon polygon = new Polygon(List.of(
                List.of(
                        new Position(0.0, 0.0),
                        new Position(10.0, 0.0),
                        new Position(10.0, 10.0),
                        new Position(0.0, 10.0),
                        new Position(0.0, 0.0)
                ),
                List.of(
                        new Position(2.0, 2.0),
                        new Position(8.0, 2.0),
                        new Position(8.0, 8.0),
                        new Position(2.0, 8.0),
                        new Position(2.0, 2.0)
                )
        ));
        String wkt = serializer.toWkt(polygon);
        assertEquals("POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))", wkt);
    }

    @Test
    void testSerializePolygonEmpty() {
        Polygon polygon = new Polygon(List.of());
        String wkt = serializer.toWkt(polygon);
        assertEquals("POLYGON EMPTY", wkt);
    }

    @Test
    void testSerializePolygonEwkt() {
        Polygon polygon = new Polygon(List.of(
                List.of(
                        new Position(0.0, 0.0),
                        new Position(10.0, 0.0),
                        new Position(10.0, 10.0),
                        new Position(0.0, 0.0)
                )
        ), CoordinateReferenceSystem.WGS_84);
        String ewkt = serializer.toEwkt(polygon);
        assertEquals("SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))", ewkt);
    }

    // MultiPoint Tests
    @Test
    void testSerializeMultiPoint() {
        MultiPoint multiPoint = new MultiPoint(List.of(
                new Position(10.0, 20.0),
                new Position(30.0, 40.0)
        ));
        String wkt = serializer.toWkt(multiPoint);
        assertEquals("MULTIPOINT ((10.0 20.0), (30.0 40.0))", wkt);
    }

    @Test
    void testSerializeMultiPointSingle() {
        MultiPoint multiPoint = new MultiPoint(List.of(
                new Position(10.0, 20.0)
        ));
        String wkt = serializer.toWkt(multiPoint);
        assertEquals("MULTIPOINT ((10.0 20.0))", wkt);
    }

    @Test
    void testSerializeMultiPointEmpty() {
        MultiPoint multiPoint = new MultiPoint(List.of());
        String wkt = serializer.toWkt(multiPoint);
        assertEquals("MULTIPOINT EMPTY", wkt);
    }

    @Test
    void testSerializeMultiPointWithAltitude() {
        MultiPoint multiPoint = new MultiPoint(List.of(
                new Position(10.0, 20.0, 5.0),
                new Position(30.0, 40.0, 15.0)
        ));
        String wkt = serializer.toWkt(multiPoint);
        assertEquals("MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))", wkt);
    }

    @Test
    void testSerializeMultiPointEwkt() {
        MultiPoint multiPoint = new MultiPoint(List.of(
                new Position(10.0, 20.0),
                new Position(30.0, 40.0)
        ), CoordinateReferenceSystem.WEB_MERCATOR);
        String ewkt = serializer.toEwkt(multiPoint);
        assertEquals("SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))", ewkt);
    }

    // MultiLineString Tests
    @Test
    void testSerializeMultiLineString() {
        MultiLineString multiLineString = new MultiLineString(List.of(
                List.of(new Position(10.0, 20.0), new Position(30.0, 40.0)),
                List.of(new Position(50.0, 60.0), new Position(70.0, 80.0))
        ));
        String wkt = serializer.toWkt(multiLineString);
        assertEquals("MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))", wkt);
    }

    @Test
    void testSerializeMultiLineStringSingle() {
        MultiLineString multiLineString = new MultiLineString(List.of(
                List.of(new Position(10.0, 20.0), new Position(30.0, 40.0), new Position(50.0, 60.0))
        ));
        String wkt = serializer.toWkt(multiLineString);
        assertEquals("MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))", wkt);
    }

    @Test
    void testSerializeMultiLineStringEmpty() {
        MultiLineString multiLineString = new MultiLineString(List.of());
        String wkt = serializer.toWkt(multiLineString);
        assertEquals("MULTILINESTRING EMPTY", wkt);
    }

    @Test
    void testSerializeMultiLineStringEwkt() {
        MultiLineString multiLineString = new MultiLineString(List.of(
                List.of(new Position(10.0, 20.0), new Position(30.0, 40.0))
        ), CoordinateReferenceSystem.WGS_84);
        String ewkt = serializer.toEwkt(multiLineString);
        assertEquals("SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))", ewkt);
    }

    // MultiPolygon Tests
    @Test
    void testSerializeMultiPolygon() {
        MultiPolygon multiPolygon = new MultiPolygon(List.of(
                List.of(
                        List.of(
                                new Position(0.0, 0.0),
                                new Position(10.0, 0.0),
                                new Position(10.0, 10.0),
                                new Position(0.0, 10.0),
                                new Position(0.0, 0.0)
                        )
                ),
                List.of(
                        List.of(
                                new Position(20.0, 20.0),
                                new Position(30.0, 20.0),
                                new Position(30.0, 30.0),
                                new Position(20.0, 30.0),
                                new Position(20.0, 20.0)
                        )
                )
        ));
        String wkt = serializer.toWkt(multiPolygon);
        assertEquals("MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))", wkt);
    }

    @Test
    void testSerializeMultiPolygonSingle() {
        MultiPolygon multiPolygon = new MultiPolygon(List.of(
                List.of(
                        List.of(
                                new Position(0.0, 0.0),
                                new Position(10.0, 0.0),
                                new Position(10.0, 10.0),
                                new Position(0.0, 0.0)
                        )
                )
        ));
        String wkt = serializer.toWkt(multiPolygon);
        assertEquals("MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))", wkt);
    }

    @Test
    void testSerializeMultiPolygonWithHoles() {
        MultiPolygon multiPolygon = new MultiPolygon(List.of(
                List.of(
                        List.of(
                                new Position(0.0, 0.0),
                                new Position(10.0, 0.0),
                                new Position(10.0, 10.0),
                                new Position(0.0, 10.0),
                                new Position(0.0, 0.0)
                        ),
                        List.of(
                                new Position(2.0, 2.0),
                                new Position(8.0, 2.0),
                                new Position(8.0, 8.0),
                                new Position(2.0, 2.0)
                        )
                )
        ));
        String wkt = serializer.toWkt(multiPolygon);
        assertEquals("MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 2.0)))", wkt);
    }

    @Test
    void testSerializeMultiPolygonEmpty() {
        MultiPolygon multiPolygon = new MultiPolygon(List.of());
        String wkt = serializer.toWkt(multiPolygon);
        assertEquals("MULTIPOLYGON EMPTY", wkt);
    }

    @Test
    void testSerializeMultiPolygonEwkt() {
        MultiPolygon multiPolygon = new MultiPolygon(List.of(
                List.of(
                        List.of(
                                new Position(0.0, 0.0),
                                new Position(10.0, 0.0),
                                new Position(10.0, 10.0),
                                new Position(0.0, 0.0)
                        )
                )
        ), CoordinateReferenceSystem.WEB_MERCATOR);
        String ewkt = serializer.toEwkt(multiPolygon);
        assertEquals("SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))", ewkt);
    }

    // GeometryCollection Tests
    @Test
    void testSerializeGeometryCollection() {
        GeometryCollection collection = new GeometryCollection(List.of(
                new Point(10.0, 20.0),
                new LineString(List.of(new Position(30.0, 40.0), new Position(50.0, 60.0)))
        ));
        String wkt = serializer.toWkt(collection);
        assertEquals("GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))", wkt);
    }

    @Test
    void testSerializeGeometryCollectionMixed() {
        GeometryCollection collection = new GeometryCollection(List.of(
                new Point(10.0, 20.0),
                new LineString(List.of(new Position(30.0, 40.0), new Position(50.0, 60.0))),
                new Polygon(List.of(
                        List.of(
                                new Position(0.0, 0.0),
                                new Position(5.0, 0.0),
                                new Position(5.0, 5.0),
                                new Position(0.0, 5.0),
                                new Position(0.0, 0.0)
                        )
                ))
        ));
        String wkt = serializer.toWkt(collection);
        assertEquals("GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))", wkt);
    }

    @Test
    void testSerializeGeometryCollectionEmpty() {
        GeometryCollection collection = new GeometryCollection(List.of());
        String wkt = serializer.toWkt(collection);
        assertEquals("GEOMETRYCOLLECTION EMPTY", wkt);
    }

    @Test
    void testSerializeGeometryCollectionEwkt() {
        GeometryCollection collection = new GeometryCollection(List.of(
                new Point(10.0, 20.0, CoordinateReferenceSystem.WEB_MERCATOR),
                new LineString(List.of(new Position(30.0, 40.0), new Position(50.0, 60.0)), CoordinateReferenceSystem.WEB_MERCATOR)
        ), CoordinateReferenceSystem.WEB_MERCATOR);
        String ewkt = serializer.toEwkt(collection);
        assertEquals("SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))", ewkt);
    }

    @Test
    void testSerializeGeometryCollectionWithMultiGeometries() {
        GeometryCollection collection = new GeometryCollection(List.of(
                new MultiPoint(List.of(new Position(1.0, 2.0), new Position(3.0, 4.0))),
                new MultiLineString(List.of(
                        List.of(new Position(10.0, 20.0), new Position(30.0, 40.0))
                ))
        ));
        String wkt = serializer.toWkt(collection);
        assertEquals("GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))", wkt);
    }
}

