package dev.goerner.geozen.model;

import dev.goerner.geozen.model.simple_geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FeatureTest {

	@Test
	public void testGeometryConstructor() {
		Geometry geometry = new Point(0.0, 0.0);
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
		Geometry geometry = new Point(0.0, 0.0);
		Feature feature = new Feature("123", geometry);

		assertEquals("123", feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertNull(feature.getProperties());
	}

	@Test
	public void testGeometryAndPropertiesConstructor() {
		Geometry geometry = new Point(0.0, 0.0);
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature(geometry, properties);

		assertNull(feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}

	@Test
	public void testIdAndGeometryAndPropertiesConstructor() {
		Geometry geometry = new Point(0.0, 0.0);
		Map<String, String> properties = new HashMap<>();
		properties.put("key", "value");
		Feature feature = new Feature("123", geometry, properties);

		assertEquals("123", feature.getId());
		assertEquals(geometry, feature.getGeometry());
		assertEquals(properties, feature.getProperties());
	}
}
