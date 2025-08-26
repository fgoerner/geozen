package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Point;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceCalculatorTest {

	@Test
	void testKarneyDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);

		double distance = DistanceCalculator.karneyDistance(p1, p2);

		assertEquals(34782.423470150534, distance);
	}

	@Test
	void testHaversineDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);

		double haversineDistance = DistanceCalculator.haversineDistance(p1, p2);

		assertEquals(34701.39385602524, haversineDistance);
	}
}
