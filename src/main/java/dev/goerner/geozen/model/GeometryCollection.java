package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link GeometryCollection} is a collection of {@link Geometry Geometries}.
 */
public class GeometryCollection {

	private ArrayList<Geometry> geometries;

	/**
	 * Creates a new empty {@link GeometryCollection}.
	 */
	public GeometryCollection() {
		this.geometries = new ArrayList<>();
	}

	/**
	 * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries}.
	 *
	 * @param geometries The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
	 */
	public GeometryCollection(ArrayList<Geometry> geometries) {
		this.geometries = geometries;
	}

	public void addGeometry(Geometry geometry) {
		this.geometries.add(geometry);
	}

	public ArrayList<Geometry> getGeometries() {
		return geometries;
	}

	public void setGeometries(ArrayList<Geometry> geometries) {
		this.geometries = geometries;
	}
}
