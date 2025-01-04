package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeometryCollectionTest {

	@Test
	public void testEmptyConstructor() {
		GeometryCollection geometryCollection = new GeometryCollection();

		assertEquals(0, geometryCollection.getGeometries().size());
	}

	@Test
	public void testGeometriesConstructor() {
		Geometry point = new Point();
		Geometry lineString = new LineString();
		ArrayList<Geometry> geometries = new ArrayList<>();
		geometries.add(point);
		geometries.add(lineString);

		GeometryCollection geometryCollection = new GeometryCollection(geometries);

		assertEquals(2, geometryCollection.getGeometries().size());
	}

	@Test
	public void testAddGeometry() {
		GeometryCollection geometryCollection = new GeometryCollection();
		Geometry point = new Point();
		Geometry lineString = new LineString();

		geometryCollection.addGeometry(point);
		geometryCollection.addGeometry(lineString);

		assertEquals(2, geometryCollection.getGeometries().size());
	}

	@Test
	public void testSetGeometries() {
		GeometryCollection geometryCollection = new GeometryCollection();
		Geometry point = new Point();
		Geometry lineString = new LineString();
		ArrayList<Geometry> geometries = new ArrayList<>();
		geometries.add(point);
		geometries.add(lineString);

		geometryCollection.setGeometries(geometries);

		assertEquals(geometries, geometryCollection.getGeometries());
	}
}
