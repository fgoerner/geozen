package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FeatureTest {

	@Test
	public void testEmptyConstructor() {
		Feature feature = new Feature();

		assertNull(feature.getId());
		assertNull(feature.getGeometry());
		assertNull(feature.getProperties());
	}

	@Test
	public void testIdConstructor() {
		Feature feature = new Feature("123");

		assertEquals("123", feature.getId());
		assertNull(feature.getGeometry());
		assertNull(feature.getProperties());
	}

	@Test
	public void testGeometryConstructor() {
		Geometry geometry = new Point();
		Feature feature = new Feature(geometry);

		assertNull(feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertNull(feature.getProperties());
	}

	@Test
	public void testPropertiesConstructor() {
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature(properties);

		assertNull(feature.getId());
		assertNull(feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}

	@Test
	public void testIdAndPropertiesConstructor() {
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature("123", properties);

		assertEquals("123", feature.getId());
		assertNull(feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}

	@Test
	public void testIdAndGeometryConstructor() {
		Geometry geometry = new Point();
		Feature feature = new Feature("123", geometry);

		assertEquals("123", feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertNull(feature.getProperties());
	}

	@Test
	public void testGeometryAndPropertiesConstructor() {
		Geometry geometry = new Point();
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature(geometry, properties);

		assertNull(feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}

	@Test
	public void testIdAndGeometryAndPropertiesConstructor() {
		Geometry geometry = new Point();
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature("123", geometry, properties);

		assertEquals("123", feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}

	@Test
	public void testSetId() {
		Feature feature = new Feature();

		feature.setId("123");

		assertEquals("123", feature.getId());
	}

	@Test
	public void testSetGeometry() {
		Feature feature = new Feature();
		Geometry geometry = new Point();

		feature.setGeometry(geometry);

		assertEquals(geometry, feature.getGeometry());
	}

	@Test
	public void testSetProperties() {
		Feature feature = new Feature();
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");

		feature.setProperties(properties);

		assertEquals(properties, feature.getProperties());
	}
}
