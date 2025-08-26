package dev.goerner.geozen.model;

/**
 * A {@link Geometry} represents spatial objects in space. It is defined by a {@link CoordinateReferenceSystem} and by
 * some data structure of {@link Position Positions} provided by its descendants, that define the shape of the spatial
 * object.
 */
public abstract sealed class Geometry
		permits Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, GeometryCollection {
	private final CoordinateReferenceSystem coordinateReferenceSystem;

	public Geometry(CoordinateReferenceSystem coordinateReferenceSystem) {
		this.coordinateReferenceSystem = coordinateReferenceSystem;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}
}
