package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiLineStringTest {

	@Test
	public void testEmptyConstructor() {
		MultiLineString multiLineString = new MultiLineString();

		assertEquals(0, multiLineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesConstructor() {
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		ArrayList<Position> lineString = new ArrayList<>();
		lineString.add(new Position(1.0, 2.0, 3.0));
		lineString.add(new Position(4.0, 5.0, 6.0));
		coordinates.add(lineString);

		MultiLineString multiLineString = new MultiLineString(coordinates);

		assertEquals(1, multiLineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesAndReferenceSystemConstructor() {
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		ArrayList<Position> lineString = new ArrayList<>();
		lineString.add(new Position(1.0, 2.0, 3.0));
		lineString.add(new Position(4.0, 5.0, 6.0));
		coordinates.add(lineString);

		MultiLineString multiLineString = new MultiLineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(1, multiLineString.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiLineString.getCoordinateReferenceSystem());
	}

	@Test
	public void testAddLineString() {
		MultiLineString multiLineString = new MultiLineString();
		ArrayList<Position> lineString = new ArrayList<>();
		lineString.add(new Position(1.0, 2.0, 3.0));
		lineString.add(new Position(4.0, 5.0, 6.0));

		multiLineString.addLineString(lineString);

		assertEquals(1, multiLineString.getCoordinates().size());
		assertEquals(lineString, multiLineString.getCoordinates().getFirst());
	}

	@Test
	public void testAddLineStringWithLineString() {
		MultiLineString multiLineString = new MultiLineString();
		ArrayList<Position> lineString = new ArrayList<>();
		lineString.add(new Position(1.0, 2.0, 3.0));
		lineString.add(new Position(4.0, 5.0, 6.0));

		multiLineString.addLineString(new LineString(lineString));

		assertEquals(1, multiLineString.getCoordinates().size());
		assertEquals(lineString, multiLineString.getCoordinates().getFirst());
	}

	@Test
	public void testSetCoordinates() {
		MultiLineString multiLineString = new MultiLineString();
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		ArrayList<Position> lineString = new ArrayList<>();
		lineString.add(new Position(1.0, 2.0, 3.0));
		lineString.add(new Position(4.0, 5.0, 6.0));
		coordinates.add(lineString);

		multiLineString.setCoordinates(coordinates);

		assertEquals(1, multiLineString.getCoordinates().size());
	}
}
