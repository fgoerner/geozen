package dev.goerner.geozen.model;

/**
 * A {@link Geometry} represents spatial objects in space. It is defined by a {@link CoordinateReferenceSystem} and by
 * some data structure of {@link Position Positions} provided by its descendants, that define the shape of the spatial
 * object.
 */
public abstract class Geometry<T> {
	private final CoordinateReferenceSystem coordinateReferenceSystem;
	protected T coordinates;

	public Geometry(CoordinateReferenceSystem coordinateReferenceSystem, T coordinates) {
		this.coordinateReferenceSystem = coordinateReferenceSystem;
		this.coordinates = coordinates;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}

	public T getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(T coordinates) {
		this.coordinates = coordinates;
	}
}
