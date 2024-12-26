package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolygonTest {

	@Test
	public void testEmptyConstructor() {
		Polygon polygon = new Polygon();

		assertEquals(0, polygon.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, polygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testExteriorRingConstructor() {
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		Polygon polygon = new Polygon(exteriorRing);

		assertEquals(1, polygon.getCoordinates().size());
		assertEquals(exteriorRing, polygon.getCoordinates().getFirst());
		assertEquals(CoordinateReferenceSystem.WGS_84, polygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testReferenceSystemConstructor() {
		Polygon polygon = new Polygon(CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testExteriorRingAndReferenceSystemConstructor() {
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		Polygon polygon = new Polygon(exteriorRing, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(1, polygon.getCoordinates().size());
		assertEquals(exteriorRing, polygon.getCoordinates().getFirst());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testSetCoordinates() {
		Polygon polygon = new Polygon();
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		coordinates.add(new ArrayList<>());
		coordinates.getFirst().add(new Position(1.0, 2.0, 3.0));
		coordinates.getFirst().add(new Position(4.0, 5.0, 6.0));

		polygon.setCoordinates(coordinates);

		assertEquals(1, polygon.getCoordinates().size());
		assertEquals(coordinates, polygon.getCoordinates());
	}

	@Test
	public void testGetExteriorRing() {
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		Polygon polygon = new Polygon(exteriorRing);

		assertEquals(exteriorRing, polygon.getExteriorRing());
	}

	@Test
	public void testSetExteriorRing() {
		Polygon polygon = new Polygon();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));

		polygon.setExteriorRing(exteriorRing);

		assertEquals(exteriorRing, polygon.getExteriorRing());
	}
}
