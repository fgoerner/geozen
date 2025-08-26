package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PositionTest {

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
}
