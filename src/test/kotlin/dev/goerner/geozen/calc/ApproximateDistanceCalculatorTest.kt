package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ApproximateDistanceCalculatorTest : FunSpec({

    test("Point to Point distance") {
        //given
        val p1 = Point(11.4694, 49.2965)
        val p2 = Point(11.0549, 49.4532)

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(p1, p2)

        //then
        approximateDistance shouldBe 34701.39385602524
    }

    test("Point to LineString distance") {
        //given
        val p1 = Point(11.4694, 49.2965)
        val lineString = LineString(
            listOf(
                Position(11.4432, 49.3429),
                Position(11.4463, 49.1877),
                Position(11.5161, 49.1239)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(p1, lineString)

        //then
        approximateDistance shouldBe 1832.5414860629317
    }

    test("Point to Polygon distance - point outside polygon") {
        //given
        val point = Point(11.4694, 49.2965)
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3) // Close the ring
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, polygon)

        //then
        approximateDistance shouldBe 2252.7607736674404
    }

    test("Point to Polygon distance - point inside polygon") {
        //given
        val point = Point(11.55, 49.35)
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3) // Close the ring
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, polygon)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to Polygon distance - point inside hole") {
        //given
        val point = Point(11.55, 49.35)
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Interior ring (hole)
                listOf(
                    Position(11.52, 49.32),
                    Position(11.58, 49.32),
                    Position(11.58, 49.38),
                    Position(11.52, 49.38),
                    Position(11.52, 49.32)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, polygon)

        //then
        approximateDistance shouldBe 2173.096197532901
    }

    test("LineString to LineString distance - non-intersecting") {
        //given
        val lineString1 = LineString(
            listOf(
                Position(11.4432, 49.3429),
                Position(11.4463, 49.1877),
                Position(11.5161, 49.1239)
            )
        )
        val lineString2 = LineString(
            listOf(
                Position(11.6, 49.3),
                Position(11.7, 49.35),
                Position(11.8, 49.3)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString1, lineString2)

        //then
        approximateDistance shouldBe 11306.646230126744
    }

    test("LineString to LineString distance - close parallel lines") {
        //given
        val lineString1 = LineString(
            listOf(
                Position(11.5, 49.3),
                Position(11.6, 49.3)
            )
        )
        val lineString2 = LineString(
            listOf(
                Position(11.5, 49.31),
                Position(11.6, 49.31)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString1, lineString2)

        //then
        approximateDistance shouldBe 1111.9508004064583
    }

    test("LineString to LineString distance - intersecting lines with common point") {
        //given
        val lineString1 = LineString(
            listOf(
                Position(11.5, 49.3),
                Position(11.55, 49.35),  // Common point
                Position(11.6, 49.4)
            )
        )
        val lineString2 = LineString(
            listOf(
                Position(11.5, 49.4),
                Position(11.55, 49.35),  // Common point
                Position(11.6, 49.3)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString1, lineString2)

        //then
        approximateDistance shouldBe 0.0
    }

    test("LineString to LineString distance - intersecting segments without common point") {
        //given
        // Two line segments that cross each other (X pattern)
        val lineString1 = LineString(
            listOf(
                Position(11.5, 49.3),   // Bottom-left
                Position(11.6, 49.4)    // Top-right
            )
        )
        val lineString2 = LineString(
            listOf(
                Position(11.5, 49.4),   // Top-left
                Position(11.6, 49.3)    // Bottom-right
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString1, lineString2)

        //then
        // These segments cross in the middle, so distance should be 0.0
        approximateDistance shouldBe 0.0
    }

    test("LineString to LineString distance - closest points in middle of segments") {
        //given
        // Two nearly parallel segments where closest points are in the middle, not at endpoints
        val lineString1 = LineString(
            listOf(
                Position(11.5, 49.3),
                Position(11.6, 49.3)
            )
        )
        val lineString2 = LineString(
            listOf(
                Position(11.51, 49.31),
                Position(11.59, 49.31)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString1, lineString2)

        //then
        // Distance should be approximately 1112 meters (about 0.01 degrees latitude difference)
        // This verifies that we're finding the minimum distance between segment interiors,
        // not just checking endpoint-to-segment distances
        approximateDistance shouldBe 1111.9508004064583
    }

    test("LineString to Polygon distance - LineString outside polygon") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.4, 49.3),
                Position(11.45, 49.35)
            )
        )
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString is completely outside the polygon
        approximateDistance shouldBe 3621.8269538835966
    }

    test("LineString to Polygon distance - LineString intersects exterior ring") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.45, 49.35),
                Position(11.55, 49.35)  // Crosses into polygon
            )
        )
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString intersects the polygon boundary
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString fully contained in polygon") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.52, 49.32),
                Position(11.58, 49.38)
            )
        )
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // All vertices are inside the polygon
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString intersects hole") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.53, 49.33),
                Position(11.57, 49.37)  // Crosses hole boundary
            )
        )
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Interior ring (hole)
                listOf(
                    Position(11.54, 49.34),
                    Position(11.56, 49.34),
                    Position(11.56, 49.36),
                    Position(11.54, 49.36),
                    Position(11.54, 49.34)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString intersects the hole boundary
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString vertex in hole") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.55, 49.35),  // Inside the hole
                Position(11.58, 49.38)
            )
        )
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Interior ring (hole)
                listOf(
                    Position(11.52, 49.32),
                    Position(11.58, 49.32),
                    Position(11.58, 49.38),
                    Position(11.52, 49.38),
                    Position(11.52, 49.32)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // First vertex is inside the hole, so calculate distance to hole boundary
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString touches polygon vertex") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.45, 49.35),
                Position(11.5, 49.4)  // Touches polygon vertex
            )
        )
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString touches polygon at a vertex (shared endpoint)
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - Complex polygon with multiple holes") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.51, 49.31),
                Position(11.59, 49.39)
            )
        )
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // First hole
                listOf(
                    Position(11.52, 49.32),
                    Position(11.54, 49.32),
                    Position(11.54, 49.34),
                    Position(11.52, 49.34),
                    Position(11.52, 49.32)
                ),
                // Second hole
                listOf(
                    Position(11.56, 49.36),
                    Position(11.58, 49.36),
                    Position(11.58, 49.38),
                    Position(11.56, 49.38),
                    Position(11.56, 49.36)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString passes between two holes, all vertices inside polygon but not in holes
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString completely contained in hole without touching") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.53, 49.33),
                Position(11.55, 49.35)
            )
        )
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Interior ring (hole) - LineString is completely inside this hole
                listOf(
                    Position(11.52, 49.32),
                    Position(11.58, 49.32),
                    Position(11.58, 49.38),
                    Position(11.52, 49.38),
                    Position(11.52, 49.32)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // LineString is completely inside the hole, so distance is to the hole boundary
        approximateDistance shouldBe 724.6598441791674
    }

    test("LineString to Polygon distance - LineString segment touches polygon vertex") {
        //given
        val lineString = LineString(
            listOf(
                Position(11.45, 49.3),
                Position(11.65, 49.3)  // Segment passes through polygon vertex at (11.5, 49.3)
            )
        )
        val polygon = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),   // This vertex lies on the LineString segment
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // Polygon vertex lies on LineString segment (collinear and within bounds)
        approximateDistance shouldBe 0.0
    }

    test("LineString to Polygon distance - LineString in non-convex hole with protruding vertex") {
        //given
        // LineString running horizontally through a hole
        val lineString = LineString(
            listOf(
                Position(11.53, 49.35),
                Position(11.57, 49.35)
            )
        )
        val polygon = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Non-convex hole with a vertex protruding toward the LineString
                listOf(
                    Position(11.52, 49.32),
                    Position(11.58, 49.32),
                    Position(11.58, 49.38),
                    Position(11.55, 49.352),  // This vertex protrudes very close to the LineString
                    Position(11.52, 49.38),
                    Position(11.52, 49.32)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(lineString, polygon)

        //then
        // The minimum distance should be from the protruding hole vertex to the LineString segment
        // This tests that we calculate distances in both directions (hole vertex → LineString)
        approximateDistance shouldBe 222.39016872618626
    }

    test("Polygon to Polygon distance - disjoint polygons") {
        //given
        val polygon1 = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.7, 49.3),
                    Position(11.8, 49.3),
                    Position(11.8, 49.4),
                    Position(11.7, 49.4),
                    Position(11.7, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // Distance between the closest vertices (11.6, 49.3) and (11.7, 49.3)
        approximateDistance shouldBe 7236.288600808812
    }

    test("Polygon to Polygon distance - touching at vertex") {
        //given
        val polygon1 = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.6, 49.3),
                    Position(11.7, 49.3),
                    Position(11.7, 49.4),
                    Position(11.6, 49.4),
                    Position(11.6, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // Polygons share a vertex - boundaries intersect
        approximateDistance shouldBe 0.0
    }

    test("Polygon to Polygon distance - edge intersection") {
        //given
        val polygon1 = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.55, 49.35),
                    Position(11.65, 49.35),
                    Position(11.65, 49.45),
                    Position(11.55, 49.45),
                    Position(11.55, 49.35)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // Polygons have overlapping boundaries
        approximateDistance shouldBe 0.0
    }

    test("Polygon to Polygon distance - one polygon fully contained in another") {
        //given
        val polygon1 = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.7, 49.3),
                    Position(11.7, 49.5),
                    Position(11.5, 49.5),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.55, 49.35),
                    Position(11.65, 49.35),
                    Position(11.65, 49.45),
                    Position(11.55, 49.45),
                    Position(11.55, 49.35)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // polygon2 is fully contained within polygon1
        approximateDistance shouldBe 0.0
    }

    test("Polygon to Polygon distance - polygon inside hole of another polygon") {
        //given
        val polygon1 = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.7, 49.3),
                    Position(11.7, 49.5),
                    Position(11.5, 49.5),
                    Position(11.5, 49.3)
                ),
                // Interior ring (hole)
                listOf(
                    Position(11.52, 49.32),
                    Position(11.68, 49.32),
                    Position(11.68, 49.48),
                    Position(11.52, 49.48),
                    Position(11.52, 49.32)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.55, 49.35),
                    Position(11.65, 49.35),
                    Position(11.65, 49.45),
                    Position(11.55, 49.45),
                    Position(11.55, 49.35)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // polygon2 is inside the hole of polygon1 - calculate distance to hole boundary
        // Closest distance is from polygon2's edge at (11.55, 49.35) to hole boundary at (11.52, 49.35)
        approximateDistance shouldBe 2168.6755984489805
    }

    test("Polygon to Polygon distance - both polygons have holes") {
        //given
        val polygon1 = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                ),
                // Hole
                listOf(
                    Position(11.52, 49.32),
                    Position(11.58, 49.32),
                    Position(11.58, 49.38),
                    Position(11.52, 49.38),
                    Position(11.52, 49.32)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                // Exterior ring
                listOf(
                    Position(11.7, 49.3),
                    Position(11.8, 49.3),
                    Position(11.8, 49.4),
                    Position(11.7, 49.4),
                    Position(11.7, 49.3)
                ),
                // Hole
                listOf(
                    Position(11.72, 49.32),
                    Position(11.78, 49.32),
                    Position(11.78, 49.38),
                    Position(11.72, 49.38),
                    Position(11.72, 49.32)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // Distance between closest vertices of exterior rings
        approximateDistance shouldBe 7236.288600808812
    }

    test("Polygon to Polygon distance - complex nested scenario") {
        //given
        val polygon1 = Polygon(
            listOf(
                // Large exterior ring
                listOf(
                    Position(11.5, 49.3),
                    Position(11.8, 49.3),
                    Position(11.8, 49.5),
                    Position(11.5, 49.5),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                // Exterior ring with hole
                listOf(
                    Position(11.52, 49.32),
                    Position(11.78, 49.32),
                    Position(11.78, 49.48),
                    Position(11.52, 49.48),
                    Position(11.52, 49.32)
                ),
                // Hole in polygon2
                listOf(
                    Position(11.54, 49.34),
                    Position(11.76, 49.34),
                    Position(11.76, 49.46),
                    Position(11.54, 49.46),
                    Position(11.54, 49.34)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // polygon2 is fully contained within polygon1
        approximateDistance shouldBe 0.0
    }

    test("Polygon to Polygon distance - small gap between polygons") {
        //given
        val polygon1 = Polygon(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3),
                    Position(11.6, 49.4),
                    Position(11.5, 49.4),
                    Position(11.5, 49.3)
                )
            )
        )
        val polygon2 = Polygon(
            listOf(
                listOf(
                    Position(11.601, 49.3),
                    Position(11.7, 49.3),
                    Position(11.7, 49.4),
                    Position(11.601, 49.4),
                    Position(11.601, 49.3)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(polygon1, polygon2)

        //then
        // Small gap of ~0.001 degrees longitude
        approximateDistance shouldBe 72.36289398691734
    }

    test("Point to MultiPoint distance - point coincides with one of the multi-point positions") {
        //given
        val point = Point(11.5, 49.3)
        val multiPoint = MultiPoint(
            listOf(
                Position(11.4, 49.2),
                Position(11.5, 49.3),  // Same as point
                Position(11.6, 49.4)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPoint)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiPoint distance - point far from all multi-point positions") {
        //given
        val point = Point(11.7, 49.5)
        val multiPoint = MultiPoint(
            listOf(
                Position(11.4, 49.2),
                Position(11.45, 49.25),
                Position(11.5, 49.3)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPoint)

        //then
        // Distance to closest point (11.5, 49.3)
        approximateDistance shouldBe 26533.528834742257
    }

    test("Point to MultiPoint distance - point closest to middle position") {
        //given
        val point = Point(11.55, 49.35)
        val multiPoint = MultiPoint(
            listOf(
                Position(11.4, 49.2),
                Position(11.56, 49.36),  // Closest
                Position(11.7, 49.5)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPoint)

        //then
        approximateDistance shouldBe 1327.0392440896394
    }

    test("Point to MultiPoint distance - single point in multi-point") {
        //given
        val point = Point(11.5, 49.3)
        val multiPoint = MultiPoint(
            listOf(
                Position(11.6, 49.4)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPoint)

        //then
        approximateDistance shouldBe 13270.79151165055
    }

    test("Point to MultiPoint distance - many points in multi-point") {
        //given
        val point = Point(11.5, 49.3)
        val multiPoint = MultiPoint(
            listOf(
                Position(11.6, 49.4),
                Position(11.7, 49.5),
                Position(11.8, 49.6),
                Position(11.51, 49.31),  // Closest
                Position(11.9, 49.7)
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPoint)

        //then
        approximateDistance shouldBe 1327.4410896967995
    }

    test("Point to MultiLineString distance - point intersects one of the linestrings") {
        //given
        val point = Point(11.5, 49.3)
        val multiLineString = MultiLineString(
            listOf(
                listOf(
                    Position(11.4, 49.2),
                    Position(11.45, 49.25)
                ),
                listOf(
                    Position(11.5, 49.3),  // Same as point
                    Position(11.6, 49.4)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiLineString)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiLineString distance - point close to one linestring, far from others") {
        //given
        val point = Point(11.55, 49.35)
        val multiLineString = MultiLineString(
            listOf(
                listOf(
                    Position(11.4, 49.2),
                    Position(11.45, 49.25)
                ),
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.4)  // Closest linestring
                ),
                listOf(
                    Position(11.7, 49.5),
                    Position(11.8, 49.6)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiLineString)

        //then
        // Distance to closest linestring segment
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiLineString distance - point equidistant from multiple linestrings") {
        //given
        val point = Point(11.5, 49.35)
        val multiLineString = MultiLineString(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.3)
                ),
                listOf(
                    Position(11.5, 49.4),
                    Position(11.6, 49.4)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiLineString)

        //then
        // Point is equidistant from both linestrings (0.05 degrees)
        approximateDistance shouldBe 5559.754011716946
    }

    test("Point to MultiLineString distance - single linestring in multi-linestring") {
        //given
        val point = Point(11.55, 49.35)
        val multiLineString = MultiLineString(
            listOf(
                listOf(
                    Position(11.5, 49.3),
                    Position(11.6, 49.4)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiLineString)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiLineString distance - complex multi-linestring") {
        //given
        val point = Point(11.65, 49.35)
        val multiLineString = MultiLineString(
            listOf(
                listOf(
                    Position(11.4, 49.2),
                    Position(11.45, 49.25),
                    Position(11.5, 49.3)
                ),
                listOf(
                    Position(11.5, 49.4),
                    Position(11.55, 49.35),  // Closest segment starts here
                    Position(11.6, 49.3)
                ),
                listOf(
                    Position(11.7, 49.5),
                    Position(11.8, 49.6),
                    Position(11.9, 49.7)
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiLineString)

        //then
        approximateDistance shouldBe 6070.698112762889
    }

    test("Point to MultiPolygon distance - point inside one of the polygons") {
        //given
        val point = Point(11.55, 49.35)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    )
                ),
                listOf(
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiPolygon distance - point outside all polygons") {
        //given
        val point = Point(11.45, 49.25)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    )
                ),
                listOf(
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        // Distance to closest polygon (first one)
        approximateDistance shouldBe 6638.41062072999
    }

    test("Point to MultiPolygon distance - point inside hole of one polygon") {
        //given
        val point = Point(11.55, 49.35)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    // Exterior ring
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    ),
                    // Interior ring (hole)
                    listOf(
                        Position(11.52, 49.32),
                        Position(11.58, 49.32),
                        Position(11.58, 49.38),
                        Position(11.52, 49.38),
                        Position(11.52, 49.32)
                    )
                ),
                listOf(
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        // Distance to hole boundary of first polygon
        approximateDistance shouldBe 2173.096197532901
    }

    test("Point to MultiPolygon distance - single polygon in multi-polygon") {
        //given
        val point = Point(11.45, 49.25)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        approximateDistance shouldBe 6638.41062072999
    }

    test("Point to MultiPolygon distance - point on boundary of one polygon") {
        //given
        val point = Point(11.5, 49.3)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(11.5, 49.3),  // Point on this vertex
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    )
                ),
                listOf(
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        approximateDistance shouldBe 0.0
    }

    test("Point to MultiPolygon distance - multiple polygons with holes") {
        //given
        val point = Point(11.65, 49.45)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    // First polygon with hole
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    ),
                    listOf(
                        Position(11.52, 49.32),
                        Position(11.58, 49.32),
                        Position(11.58, 49.38),
                        Position(11.52, 49.38),
                        Position(11.52, 49.32)
                    )
                ),
                listOf(
                    // Second polygon with hole
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    ),
                    listOf(
                        Position(11.72, 49.52),
                        Position(11.78, 49.52),
                        Position(11.78, 49.58),
                        Position(11.72, 49.58),
                        Position(11.72, 49.52)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        // Distance to closest polygon (second one)
        approximateDistance shouldBe 6630.373465002598
    }

    test("Point to MultiPolygon distance - point between two polygons") {
        //given
        val point = Point(11.65, 49.45)
        val multiPolygon = MultiPolygon(
            listOf(
                listOf(
                    listOf(
                        Position(11.5, 49.3),
                        Position(11.6, 49.3),
                        Position(11.6, 49.4),
                        Position(11.5, 49.4),
                        Position(11.5, 49.3)
                    )
                ),
                listOf(
                    listOf(
                        Position(11.7, 49.5),
                        Position(11.8, 49.5),
                        Position(11.8, 49.6),
                        Position(11.7, 49.6),
                        Position(11.7, 49.5)
                    )
                )
            )
        )

        //when
        val approximateDistance = ApproximateDistanceCalculator.calculate(point, multiPolygon)

        //then
        // Distance to the closer polygon boundary
        approximateDistance shouldBe 6630.373465002598
    }
})
