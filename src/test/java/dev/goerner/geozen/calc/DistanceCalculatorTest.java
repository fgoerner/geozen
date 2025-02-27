package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DistanceCalculatorTest {

	@Test
	void testKarneyDistance() {
		Position p1 = new Position(11.4694, 49.2965);
		Position p2 = new Position(11.0549, 49.4532);

		double distance = DistanceCalculator.karneyDistance(p1, p2);

		assertEquals(34782.42347014982, distance);
	}

	@Test
	void testHaversineDistance() {
		Position p1 = new Position(11.4694, 49.2965);
		Position p2 = new Position(11.0549, 49.4532);

		double haversineDistance = DistanceCalculator.haversineDistance(p1, p2);

		assertEquals(34701.39385602524, haversineDistance);
	}

	@Test
	void testPointToPointExactDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);

		double distance = p1.getMinDistanceTo(p2, true);

		assertEquals(34782.42347014982, distance);
	}

	@Test
	void testPointToPointFastDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);

		double distance = p1.getMinDistanceTo(p2);

		assertEquals(34701.39385602524, distance);
	}

	@Test
	void testFeatureToFeatureExactDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);
		Feature feature1 = new Feature(p1);
		Feature feature2 = new Feature(p2);

		double distance = feature1.getMinDistanceTo(feature2, true);

		assertEquals(34782.42347014982, distance);
	}

	@Test
	void testFeatureToFeatureFastDistance() {
		Point p1 = new Point(11.4694, 49.2965);
		Point p2 = new Point(11.0549, 49.4532);
		Feature feature1 = new Feature(p1);
		Feature feature2 = new Feature(p2);

		double distance = feature1.getMinDistanceTo(feature2);

		assertEquals(34701.39385602524, distance);
	}
}
