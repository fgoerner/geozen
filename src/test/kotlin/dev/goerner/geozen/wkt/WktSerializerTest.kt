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

class WktSerializerTest : FunSpec({

    // Point Tests
    test("serialize point") {
        //given
        val point = Point(10.5, 20.3)

        //when
        val wkt = WktSerializer.toWkt(point)

        //then
        wkt shouldBe "POINT (10.5 20.3)"
    }

    test("serialize point with altitude") {
        //given
        val point = Point(10.5, 20.3, 100.0)

        //when
        val wkt = WktSerializer.toWkt(point)

        //then
        wkt shouldBe "POINT (10.5 20.3 100.0)"
    }

    test("serialize point EWKT WGS84") {
        //given
        val point = Point(10.5, 20.3)

        //when
        val ewkt = WktSerializer.toEwkt(point)

        //then
        ewkt shouldBe "SRID=4326;POINT (10.5 20.3)"
    }

    test("serialize point EWKT Web Mercator") {
        //given
        val point = Point(Position(10.5, 20.3), CoordinateReferenceSystem.WEB_MERCATOR)

        //when
        val ewkt = WktSerializer.toEwkt(point)

        //then
        ewkt shouldBe "SRID=3857;POINT (10.5 20.3)"
    }

    test("serialize point negative coordinates") {
        //given
        val point = Point(-122.4194, 37.7749)

        //when
        val wkt = WktSerializer.toWkt(point)

        //then
        wkt shouldBe "POINT (-122.4194 37.7749)"
    }

    // LineString Tests
    test("serialize LineString") {
        //given
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0),
                Position(50.0, 60.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(lineString)

        //then
        wkt shouldBe "LINESTRING (10.0 20.0, 30.0 40.0, 50.0 60.0)"
    }

    test("serialize LineString with altitude") {
        //given
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0, 5.0),
                Position(30.0, 40.0, 10.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(lineString)

        //then
        wkt shouldBe "LINESTRING (10.0 20.0 5.0, 30.0 40.0 10.0)"
    }

    test("serialize LineString EWKT") {
        //given
        val lineString = LineString(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )

        //when
        val ewkt = WktSerializer.toEwkt(lineString)

        //then
        ewkt shouldBe "SRID=3857;LINESTRING (10.0 20.0, 30.0 40.0)"
    }

    // Polygon Tests
    test("serialize Polygon") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(polygon)

        //then
        wkt shouldBe "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0))"
    }

    test("serialize Polygon with hole") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(polygon)

        //then
        wkt shouldBe "POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0))"
    }

    test("serialize Polygon EWKT") {
        //given
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

        //when
        val ewkt = WktSerializer.toEwkt(polygon)

        //then
        ewkt shouldBe "SRID=4326;POLYGON ((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0))"
    }

    // MultiPoint Tests
    test("serialize MultiPoint") {
        //given
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiPoint)

        //then
        wkt shouldBe "MULTIPOINT ((10.0 20.0), (30.0 40.0))"
    }

    test("serialize MultiPoint single") {
        //given
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiPoint)

        //then
        wkt shouldBe "MULTIPOINT ((10.0 20.0))"
    }

    test("serialize MultiPoint empty not supported") {
        //given
        val multiPoint = MultiPoint(emptyList())

        //when
        val action = { WktSerializer.toWkt(multiPoint) }

        //then
        shouldThrow<WktException>(action)
    }

    test("serialize MultiPoint with altitude") {
        //given
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0, 5.0),
                Position(30.0, 40.0, 15.0)
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiPoint)

        //then
        wkt shouldBe "MULTIPOINT ((10.0 20.0 5.0), (30.0 40.0 15.0))"
    }

    test("serialize MultiPoint EWKT") {
        //given
        val multiPoint = MultiPoint(
            listOf(
                Position(10.0, 20.0),
                Position(30.0, 40.0)
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )

        //when
        val ewkt = WktSerializer.toEwkt(multiPoint)

        //then
        ewkt shouldBe "SRID=3857;MULTIPOINT ((10.0 20.0), (30.0 40.0))"
    }

    // MultiLineString Tests
    test("serialize MultiLineString") {
        //given
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0)),
                listOf(Position(50.0, 60.0), Position(70.0, 80.0))
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiLineString)

        //then
        wkt shouldBe "MULTILINESTRING ((10.0 20.0, 30.0 40.0), (50.0 60.0, 70.0 80.0))"
    }

    test("serialize MultiLineString single") {
        //given
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0), Position(50.0, 60.0))
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiLineString)

        //then
        wkt shouldBe "MULTILINESTRING ((10.0 20.0, 30.0 40.0, 50.0 60.0))"
    }

    test("serialize MultiLineString empty not supported") {
        //given
        val multiLineString = MultiLineString(emptyList())

        //when
        val action = { WktSerializer.toWkt(multiLineString) }

        //then
        shouldThrow<WktException>(action)
    }

    test("serialize MultiLineString EWKT") {
        //given
        val multiLineString = MultiLineString(
            listOf(
                listOf(Position(10.0, 20.0), Position(30.0, 40.0))
            ), CoordinateReferenceSystem.WGS_84
        )

        //when
        val ewkt = WktSerializer.toEwkt(multiLineString)

        //then
        ewkt shouldBe "SRID=4326;MULTILINESTRING ((10.0 20.0, 30.0 40.0))"
    }

    // MultiPolygon Tests
    test("serialize MultiPolygon") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(multiPolygon)

        //then
        wkt shouldBe "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0)), " +
                "((20.0 20.0, 30.0 20.0, 30.0 30.0, 20.0 30.0, 20.0 20.0)))"
    }

    test("serialize MultiPolygon single") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(multiPolygon)

        //then
        wkt shouldBe "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"
    }

    test("serialize MultiPolygon with holes") {
        //given
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
                        Position(2.0, 8.0),
                        Position(2.0, 2.0)
                    )
                )
            )
        )

        //when
        val wkt = WktSerializer.toWkt(multiPolygon)

        //then
        wkt shouldBe "MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 10.0, 0.0 0.0), " +
                "(2.0 2.0, 8.0 2.0, 8.0 8.0, 2.0 8.0, 2.0 2.0)))"
    }

    test("serialize MultiPolygon empty not supported") {
        //given
        val multiPolygon = MultiPolygon(emptyList())

        //when
        val action = { WktSerializer.toWkt(multiPolygon) }

        //then
        shouldThrow<WktException>(action)
    }

    test("serialize MultiPolygon EWKT") {
        //given
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

        //when
        val ewkt = WktSerializer.toEwkt(multiPolygon)

        //then
        ewkt shouldBe "SRID=3857;MULTIPOLYGON (((0.0 0.0, 10.0 0.0, 10.0 10.0, 0.0 0.0)))"
    }

    // GeometryCollection Tests
    test("serialize GeometryCollection") {
        //given
        val collection = GeometryCollection(
            listOf(
                Point(10.0, 20.0),
                LineString(listOf(Position(30.0, 40.0), Position(50.0, 60.0)))
            )
        )

        //when
        val wkt = WktSerializer.toWkt(collection)

        //then
        wkt shouldBe "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
    }

    test("serialize GeometryCollection mixed") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(collection)

        //then
        wkt shouldBe "GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0), " +
                "POLYGON ((0.0 0.0, 5.0 0.0, 5.0 5.0, 0.0 5.0, 0.0 0.0)))"
    }

    test("serialize GeometryCollection empty not supported") {
        //given
        val collection = GeometryCollection(emptyList())

        //when
        val action = { WktSerializer.toWkt(collection) }

        //then
        shouldThrow<WktException>(action)
    }

    test("serialize GeometryCollection EWKT") {
        //given
        val collection = GeometryCollection(
            listOf(
                Point(10.0, 20.0, 0.0, CoordinateReferenceSystem.WEB_MERCATOR),
                LineString(
                    listOf(Position(30.0, 40.0), Position(50.0, 60.0)),
                    CoordinateReferenceSystem.WEB_MERCATOR
                )
            ), CoordinateReferenceSystem.WEB_MERCATOR
        )

        //when
        val ewkt = WktSerializer.toEwkt(collection)

        //then
        ewkt shouldBe "SRID=3857;GEOMETRYCOLLECTION (POINT (10.0 20.0), LINESTRING (30.0 40.0, 50.0 60.0))"
    }

    test("serialize GeometryCollection with multi geometries") {
        //given
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

        //when
        val wkt = WktSerializer.toWkt(collection)

        //then
        wkt shouldBe "GEOMETRYCOLLECTION (MULTIPOINT ((1.0 2.0), (3.0 4.0)), " +
                "MULTILINESTRING ((10.0 20.0, 30.0 40.0)))"
    }
})
