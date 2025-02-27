package dev.goerner.geozen.model;

/**
 * A {@link Geometry} represents spatial objects in space. It is defined by a {@link CoordinateReferenceSystem} and by
 * some data structure of {@link Position Positions} provided by its descendants, that define the shape of the spatial
 * object.
 */
public abstract class Geometry {

	private final CoordinateReferenceSystem coordinateReferenceSystem;

	public Geometry(CoordinateReferenceSystem coordinateReferenceSystem) {
		this.coordinateReferenceSystem = coordinateReferenceSystem;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return coordinateReferenceSystem;
	}

	public double getMinDistanceTo(Geometry geometry) {
		return getMinDistanceTo(geometry, false);
	}

	public double getMinDistanceTo(Geometry geometry, boolean exact) {
		return switch (geometry) {
			case Point point -> getMinDistanceTo(point, exact);
			case LineString lineString -> getMinDistanceTo(lineString, exact);
			case Polygon polygon -> getMinDistanceTo(polygon, exact);
			case MultiPoint multiPoint -> getMinDistanceTo(multiPoint, exact);
			case MultiLineString multiLineString -> getMinDistanceTo(multiLineString, exact);
			case MultiPolygon multiPolygon -> getMinDistanceTo(multiPolygon, exact);
			default -> 0;
		};
	}

	public abstract double getMinDistanceTo(Point point, boolean exact);

	public abstract double getMinDistanceTo(LineString lineString, boolean exact);

	public abstract double getMinDistanceTo(Polygon polygon, boolean exact);

	public abstract double getMinDistanceTo(MultiPoint multiPoint, boolean exact);

	public abstract double getMinDistanceTo(MultiLineString multiLineString, boolean exact);

	public abstract double getMinDistanceTo(MultiPolygon multiPolygon, boolean exact);
}
