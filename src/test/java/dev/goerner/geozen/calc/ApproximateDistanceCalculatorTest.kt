package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApproximateDistanceCalculatorTest {

    @Test
    void testHaversineDistance() {
        Position p1 = new Position(11.4694, 49.2965);
        Position p2 = new Position(11.0549, 49.4532);

        double haversineDistance = ApproximateDistanceCalculator.INSTANCE.haversineDistance(p1, p2);

        assertEquals(34701.39385602524, haversineDistance);
    }

    @Test
    void testApproximateDistance() {
        Point p1 = new Point(11.4694, 49.2965);
        Point p2 = new Point(11.0549, 49.4532);

        double approximateDistance = ApproximateDistanceCalculator.INSTANCE.calculate(p1, p2);

        assertEquals(34701.39385602524, approximateDistance);
    }

    @Test
    void testApproximateDistanceLineString() {
        Point p1 = new Point(11.4694, 49.2965);
        LineString lineString = new LineString(
                List.of(
                        new Position(11.4432, 49.3429),
                        new Position(11.4463, 49.1877),
                        new Position(11.5161, 49.1239)
                )
        );

        double approximateDistance = ApproximateDistanceCalculator.INSTANCE.calculate(p1, lineString);

        assertEquals(1832.5414860629317, approximateDistance);
    }
}
