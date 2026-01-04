package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PreciseDistanceCalculatorTest {

    @Test
    void testKarneyDistance() {
        Position p1 = new Position(11.4694, 49.2965);
        Position p2 = new Position(11.0549, 49.4532);

        double distance = PreciseDistanceCalculator.karneyDistance(p1, p2);

        assertEquals(34782.42347014982, distance);
    }

    @Test
    void testCalculate() {
        Point p1 = new Point(11.4694, 49.2965);
        Point p2 = new Point(11.0549, 49.4532);

        double preciseDistance = PreciseDistanceCalculator.calculate(p1, p2);

        assertEquals(34782.42347014982, preciseDistance);
    }

    @Test
    void testCalculateLineString() {
        Point p1 = new Point(11.4694, 49.2965);
        LineString lineString = new LineString(
                List.of(
                        new Position(11.4432, 49.3429),
                        new Position(11.4463, 49.1877),
                        new Position(11.5161, 49.1239)
                )
        );

        double preciseDistance = PreciseDistanceCalculator.calculate(p1, lineString);

        assertEquals(1837.9808887665006, preciseDistance);
    }
}
