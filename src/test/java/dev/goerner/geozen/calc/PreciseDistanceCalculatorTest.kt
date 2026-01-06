package dev.goerner.geozen.calc

import dev.goerner.geozen.calc.PreciseDistanceCalculator.karneyDistance
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PreciseDistanceCalculatorTest {

    @Test
    fun `test karney distance`() {
        val p1 = Position(11.4694, 49.2965)
        val p2 = Position(11.0549, 49.4532)

        val distance = karneyDistance(p1, p2)

        assertEquals(34782.42347014982, distance)
    }

    @Test
    fun `test Point to Point distance`() {
        val p1 = Point(11.4694, 49.2965)
        val p2 = Point(11.0549, 49.4532)

        val preciseDistance = PreciseDistanceCalculator.calculate(p1, p2)

        assertEquals(34782.42347014982, preciseDistance)
    }

    @Test
    fun `test Point to LineString distance`() {
        val p1 = Point(11.4694, 49.2965)
        val lineString = LineString(
            listOf(
                Position(11.4432, 49.3429),
                Position(11.4463, 49.1877),
                Position(11.5161, 49.1239)
            )
        )

        val preciseDistance = PreciseDistanceCalculator.calculate(p1, lineString)

        assertEquals(1874.0229499712636, preciseDistance)
    }
}
