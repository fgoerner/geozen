package dev.goerner.geozen.calc

import dev.goerner.geozen.calc.PreciseDistanceCalculator.karneyDistance
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class PreciseDistanceCalculatorTest : FunSpec({

    test("karney distance") {
        //given
        val p1 = Position(11.4694, 49.2965)
        val p2 = Position(11.0549, 49.4532)

        //when
        val distance = karneyDistance(p1, p2)

        //then
        distance shouldBe 34782.42347014982
    }

    test("Point to Point distance") {
        //given
        val p1 = Point(11.4694, 49.2965)
        val p2 = Point(11.0549, 49.4532)

        //when
        val preciseDistance = PreciseDistanceCalculator.calculate(p1, p2)

        //then
        preciseDistance shouldBe 34782.42347014982
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
        val preciseDistance = PreciseDistanceCalculator.calculate(p1, lineString)

        //then
        preciseDistance shouldBe 1874.0229499712636
    }
})
