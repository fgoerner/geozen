package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link MultiLineString} is a {@link Geometry} that represents a collection of {@link LineString LineStrings} in
 * space. It is defined by a list of {@link LineString LineStrings} and a {@link CoordinateReferenceSystem}.
 */
public class MultiLineString extends Geometry {

	private ArrayList<ArrayList<Position>> coordinates;

	/**
	 * Creates a new {@link MultiLineString} with an empty list of {@link LineString LineStrings} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 */
	public MultiLineString() {
		this(new ArrayList<>());
	}

	/**
	 * Creates a new {@link MultiLineString} with the given list of {@link LineString LineStrings} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The list of {@link LineString LineStrings} representing the {@link MultiLineString}.
	 */
	public MultiLineString(ArrayList<ArrayList<Position>> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link MultiLineString} with the given list of {@link LineString LineStrings} and the given
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The list of {@link LineString LineStrings} representing the {@link MultiLineString}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiLineString}.
	 */
	public MultiLineString(ArrayList<ArrayList<Position>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
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

	public void addLineString(ArrayList<Position> lineString) {
		this.coordinates.add(lineString);
	}

	public void addLineString(LineString lineString) {
		this.coordinates.add(lineString.getCoordinates());
	}

	public ArrayList<ArrayList<Position>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<ArrayList<Position>> coordinates) {
		this.coordinates = coordinates;
	}
}
