package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link MultiPoint} is a {@link Geometry} that represents a collection of {@link Position Positions} in space. It is
 * defined by a list of {@link Position Positions} and a {@link CoordinateReferenceSystem}.
 */
public class MultiPoint extends Geometry {

	private ArrayList<Position> coordinates;

	/**
	 * Creates a new {@link MultiPoint} with an empty list of {@link Position Positions} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 */
	public MultiPoint() {
		this(new ArrayList<>());
	}

	/**
	 * Creates a new {@link MultiPoint} with the given list of {@link Position Positions} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The list of {@link Position Positions} representing the {@link MultiPoint}.
	 */
	public MultiPoint(ArrayList<Position> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link MultiPoint} with the given list of {@link Position Positions} and the given
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The list of {@link Position Positions} representing the {@link MultiPoint}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiPoint}.
	 */
	public MultiPoint(ArrayList<Position> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = coordinates;
	}

	public void addPoint(Position point) {
		this.coordinates.add(point);
	}

	public void addPoint(Point point) {
		this.coordinates.add(point.getCoordinates());
	}

	public ArrayList<Position> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(ArrayList<Position> coordinates) {
		this.coordinates = coordinates;
	}
}
