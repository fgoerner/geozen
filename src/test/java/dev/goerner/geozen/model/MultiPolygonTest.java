package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPolygonTest {

	@Test
	public void testEmptyConstructor() {
		MultiPolygon multiPolygon = new MultiPolygon();

		assertEquals(0, multiPolygon.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiPolygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesConstructor() {
		ArrayList<ArrayList<ArrayList<Position>>> coordinates = new ArrayList<>();
		ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		polygon.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		polygon.add(interiorRing);
		coordinates.add(polygon);

		MultiPolygon multiPolygon = new MultiPolygon(coordinates);

		assertEquals(1, multiPolygon.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WGS_84, multiPolygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesAndReferenceSystemConstructor() {
		ArrayList<ArrayList<ArrayList<Position>>> coordinates = new ArrayList<>();
		ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		polygon.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		polygon.add(interiorRing);
		coordinates.add(polygon);

		MultiPolygon multiPolygon = new MultiPolygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(1, multiPolygon.getCoordinates().size());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPolygon.getCoordinateReferenceSystem());
	}

	@Test
	public void testAddPolygon() {
		MultiPolygon multiPolygon = new MultiPolygon();
		ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		polygon.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		polygon.add(interiorRing);

		multiPolygon.addPolygon(polygon);

		assertEquals(1, multiPolygon.getCoordinates().size());
		assertEquals(polygon, multiPolygon.getCoordinates().getFirst());
	}

	@Test
	public void testAddPolygonWithPolygon() {
		MultiPolygon multiPolygon = new MultiPolygon();
		ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		polygon.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		polygon.add(interiorRing);

		multiPolygon.addPolygon(new Polygon(polygon));

		assertEquals(1, multiPolygon.getCoordinates().size());
		assertEquals(polygon, multiPolygon.getCoordinates().getFirst());
	}

	@Test
	public void testSetCoordinates() {
		MultiPolygon multiPolygon = new MultiPolygon();
		ArrayList<ArrayList<ArrayList<Position>>> coordinates = new ArrayList<>();
		ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		polygon.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		polygon.add(interiorRing);
		coordinates.add(polygon);

		multiPolygon.setCoordinates(coordinates);

		assertEquals(1, multiPolygon.getCoordinates().size());
	}
}
