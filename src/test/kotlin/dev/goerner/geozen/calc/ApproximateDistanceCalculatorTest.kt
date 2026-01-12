package dev.goerner.geozen.calc

import dev.goerner.geozen.calc.ApproximateDistanceCalculator.haversineDistance
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ApproximateDistanceCalculatorTest {

    @Test
    fun `test haversine distance`() {
        val p1 = Position(11.4694, 49.2965)
        val p2 = Position(11.0549, 49.4532)

        val haversineDistance = haversineDistance(p1, p2)

        assertEquals(34701.39385602524, haversineDistance)
    }

    @Test
    fun `test Point to Point distance`() {
        val p1 = Point(11.4694, 49.2965)
        val p2 = Point(11.0549, 49.4532)

        val approximateDistance = ApproximateDistanceCalculator.calculate(p1, p2)

        assertEquals(34701.39385602524, approximateDistance)
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

        val approximateDistance = ApproximateDistanceCalculator.calculate(p1, lineString)

        assertEquals(1832.5414860629317, approximateDistance)
    }
}
