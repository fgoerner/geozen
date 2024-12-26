package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointTest {

	@Test
	public void testEmptyConstructor() {
		Point point = new Point();

		assertEquals(0, point.getLongitude());
		assertEquals(0, point.getLatitude());
		assertEquals(0, point.getAltitude());
		assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testLongitudeAndLatitudeConstructor() {
		Point point = new Point(1.0, 2.0);

		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals(0, point.getAltitude());
		assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testLongitudeAndLatitudeAndAltitudeConstructor() {
		Point point = new Point(1.0, 2.0, 3.0);

		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals(3.0, point.getAltitude());
		assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesConstructor() {
		Position coordinates = new Position(1.0, 2.0, 3.0);
		Point point = new Point(coordinates);

		assertEquals(coordinates, point.getCoordinates());
		assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testLongitudeAndLatitudeAndReferenceSystemConstructor() {
		Point point = new Point(1.0, 2.0, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals(0, point.getAltitude());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testLongitudeAndLatitudeAndAltitudeAndReferenceSystemConstructor() {
		Point point = new Point(1.0, 2.0, 3.0, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals(3.0, point.getAltitude());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testCoordinatesAndReferenceSystemConstructor() {
		Position coordinates = new Position(1.0, 2.0, 3.0);
		Point point = new Point(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

		assertEquals(coordinates, point.getCoordinates());
		assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
	}

	@Test
	public void testLongitudeSetter() {
		Point point = new Point();

		point.setLongitude(4.0);

		assertEquals(4.0, point.getLongitude());
	}

	@Test
	public void testLatitudeSetter() {
		Point point = new Point();

		point.setLatitude(5.0);

		assertEquals(5.0, point.getLatitude());
	}

	@Test
	public void testAltitudeSetter() {
		Point point = new Point();

		point.setAltitude(6.0);

		assertEquals(6.0, point.getAltitude());
	}

	@Test
	public void testSetCoordinates() {
		Point point = new Point();
		Position coordinates = new Position(1.0, 2.0, 3.0);

		point.setCoordinates(coordinates);

		assertEquals(coordinates, point.getCoordinates());
	}
}
