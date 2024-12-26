package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LineStringTest {

	@Test
	public void testEmptyConstructor() {
		LineString lineString = new LineString();

		assertEquals(0, lineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, lineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesConstructor() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		LineString lineString = new LineString(coordinates);

		assertEquals(2, lineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, lineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testReferenceSystemConstructor() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		LineString lineString = new LineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(2, lineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, lineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testSetCoordinates() {
		LineString lineString = new LineString();
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));

		lineString.setCoordinates(coordinates);

		assertEquals(2, lineString.getCoordinates().size());
	}

	@Test
	public void testAddCoordinate() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		LineString lineString = new LineString(coordinates);
		Position addedCoordinate = new Position(7.0, 8.0, 9.0);

		lineString.addCoordinate(addedCoordinate);

		assertEquals(3, lineString.getCoordinates().size());
		assertTrue(lineString.getCoordinates().contains(addedCoordinate));
	}

	@Test
	public void testAddCoordinates() {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		LineString lineString = new LineString(coordinates);
		Position addedCoordinate1 = new Position(7.0, 8.0, 9.0);
		Position addedCoordinate2 = new Position(10.0, 11.0, 12.0);

		lineString.addCoordinates(addedCoordinate1, addedCoordinate2);

		assertEquals(4, lineString.getCoordinates().size());
		assertTrue(lineString.getCoordinates().contains(addedCoordinate1));
		assertTrue(lineString.getCoordinates().contains(addedCoordinate2));
	}
}
