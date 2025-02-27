package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link MultiPolygon} is a {@link Geometry} that represents a collection of {@link Polygon Polygons} in space. It is
 * defined by a list of {@link Polygon Polygons} and a {@link CoordinateReferenceSystem}.
 */
public class MultiPolygon extends Geometry {

	private ArrayList<ArrayList<ArrayList<Position>>> coordinates;

	/**
	 * Creates a new {@link MultiPolygon} with an empty list of {@link Polygon Polygons} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 */
	public MultiPolygon() {
		this(new ArrayList<>());
	}

	/**
	 * Creates a new {@link MultiPolygon} with the given list of {@link Polygon Polygons} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The list of {@link Polygon Polygons} representing the {@link MultiPolygon}.
	 */
	public MultiPolygon(ArrayList<ArrayList<ArrayList<Position>>> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link MultiPolygon} with the given list of {@link Polygon Polygons} and the given
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The list of {@link Polygon Polygons} representing the {@link MultiPolygon}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiPolygon}.
	 */
	public MultiPolygon(ArrayList<ArrayList<ArrayList<Position>>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = coordinates;
	}

	@Override
	public double getMinDistanceTo(Point point, boolean exact) {
		return 0;
	}

	@Override
	public double getMinDistanceTo(LineString lineString, boolean exact) {
		return 0;
	}

	@Override
	public double getMinDistanceTo(Polygon polygon, boolean exact) {
		return 0;
	}

	@Override
	public double getMinDistanceTo(MultiPoint multiPoint, boolean exact) {
		return 0;
	}

	@Override
	public double getMinDistanceTo(MultiLineString multiLineString, boolean exact) {
		return 0;
	}

	@Override
	public double getMinDistanceTo(MultiPolygon multiPolygon, boolean exact) {
		return 0;
	}

	public void addPolygon(ArrayList<ArrayList<Position>> polygon) {
		this.coordinates.add(polygon);
	}

	public void addPolygon(Polygon polygon) {
		this.coordinates.add(polygon.getCoordinates());
	}

	public ArrayList<ArrayList<ArrayList<Position>>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<ArrayList<ArrayList<Position>>> coordinates) {
		this.coordinates = coordinates;
	}
}
