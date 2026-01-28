package dev.goerner.geozen.calc

import dev.goerner.geozen.calc.ApproximateDistanceCalculator.haversineDistance
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class ApproximateDistanceCalculatorTest : FunSpec({

    test("haversine distance") {
        //given
        val p1 = Position(11.4694, 49.2965)
        val p2 = Position(11.0549, 49.4532)

        //when
        val haversineDistance = haversineDistance(p1, p2)

        //then
        haversineDistance shouldBe 34701.39385602524
    }

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
})
