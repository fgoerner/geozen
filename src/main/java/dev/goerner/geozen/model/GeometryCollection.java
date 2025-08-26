package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link GeometryCollection} is a collection of {@link Geometry Geometries}.
 */
public class GeometryCollection extends Geometry {

	private ArrayList<Geometry> geometries;

	/**
	 * Creates a new empty {@link GeometryCollection} with the default WGS 84 {@link CoordinateReferenceSystem}.
	 */
	public GeometryCollection() {
		this(new ArrayList<>());
	}

	/**
	 * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries} and the default WGS 84 {@link CoordinateReferenceSystem}.
	 *
	 * @param geometries The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
	 */
	public GeometryCollection(ArrayList<Geometry> geometries) {
		this(geometries, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries} and {@link CoordinateReferenceSystem}.
	 *
	 * @param geometries                The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link GeometryCollection}.
	 */
	public GeometryCollection(ArrayList<Geometry> geometries, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
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
