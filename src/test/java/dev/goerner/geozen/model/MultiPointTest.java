package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPointTest {

	@Test
	public void testEmptyConstructor() {
		MultiPoint multiPoint = new MultiPoint();

		assertEquals(0, multiPoint.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiPoint.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesConstructor() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));

		MultiPoint multiPoint = new MultiPoint(coordinates);

		assertEquals(2, multiPoint.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiPoint.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesAndReferenceSystemConstructor() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));

		MultiPoint multiPoint = new MultiPoint(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(2, multiPoint.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPoint.getCoordinateReferenceSystem());
	}

	@Test
	public void testAddPoint() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		MultiPoint multiPoint = new MultiPoint(coordinates);
		Position addedPoint = new Position(7.0, 8.0, 9.0);

		multiPoint.addPoint(addedPoint);

		assertEquals(3, multiPoint.getCoordinates().size());
		assertEquals(addedPoint, multiPoint.getCoordinates().get(2));
	}

	@Test
	public void testAddPointWithPoint() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		MultiPoint multiPoint = new MultiPoint(coordinates);
		Position addedPoint = new Position(7.0, 8.0, 9.0);
		Point point = new Point(addedPoint);

		multiPoint.addPoint(point);

		assertEquals(3, multiPoint.getCoordinates().size());
		assertEquals(addedPoint, multiPoint.getCoordinates().get(2));
	}

	@Test
	public void testSetCoordinates() {
		MultiPoint multiPoint = new MultiPoint();
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));

		multiPoint.setCoordinates(coordinates);

		assertEquals(2, multiPoint.getCoordinates().size());
	}
}
