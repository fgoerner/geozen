package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

	@Test
	public void testEmptyConstructor() {
		Position position = new Position();

		assertEquals(0, position.getLongitude());
		assertEquals(0, position.getLatitude());
		assertEquals(0, position.getAltitude());
	}

	@Test
	public void testLongitudeAndLatitudeConstructor() {
		Position position = new Position(1.0, 2.0);

		assertEquals(1.0, position.getLongitude());
		assertEquals(2.0, position.getLatitude());
		assertEquals(0, position.getAltitude());
	}

	@Test
	public void testLongitudeAndLatitudeAndAltitudeConstructor() {
		Position position = new Position(1.0, 2.0, 3.0);

		assertEquals(1.0, position.getLongitude());
		assertEquals(2.0, position.getLatitude());
		assertEquals(3.0, position.getAltitude());
	}

	@Test
	public void testGetLongitude() {
		Position position = new Position(1.0, 2.0, 3.0);

		assertEquals(1.0, position.getLongitude());
	}

	@Test
	public void testGetLatitude() {
		Position position = new Position(1.0, 2.0, 3.0);

		assertEquals(2.0, position.getLatitude());
	}

	@Test
	public void testGetAltitude() {
		Position position = new Position(1.0, 2.0, 3.0);

		assertEquals(3.0, position.getAltitude());
	}

	@Test
	public void testSetLongitude() {
		Position position = new Position();

		position.setLongitude(4.0);

		assertEquals(4.0, position.getLongitude());
	}

	@Test
	public void testSetLatitude() {
		Position position = new Position();

		position.setLatitude(5.0);

		assertEquals(5.0, position.getLatitude());
	}

	@Test
	public void testSetAltitude() {
		Position position = new Position();

		position.setAltitude(6.0);

		assertEquals(6.0, position.getAltitude());
	}
}
