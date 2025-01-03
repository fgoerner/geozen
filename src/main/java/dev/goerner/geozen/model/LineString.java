package dev.goerner.geozen.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A {@link LineString} is a {@link Geometry} that represents a sequence of {@link Position Positions} in space. It is
 * defined by a list of {@link Position Positions} and a {@link CoordinateReferenceSystem}.
 */
public class LineString extends Geometry {

	private ArrayList<Position> coordinates;

	/**
	 * Creates a new {@link LineString} with an empty list of {@link Position Positions} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 */
	public LineString() {
		this(new ArrayList<>());
	}

	/**
	 * Creates a new {@link LineString} with the given list of {@link Position Positions} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The list of {@link Position Positions} representing the {@link LineString}.
	 */
	public LineString(ArrayList<Position> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link LineString} with the given list of {@link Position Positions} and the given
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The list of {@link Position Positions} representing the {@link LineString}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link LineString}.
	 */
	public LineString(ArrayList<Position> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = coordinates;
	}

	public void addCoordinate(Position coordinate) {
		this.coordinates.add(coordinate);
	}

	public void addCoordinates(Position... coordinates) {
		this.coordinates.ensureCapacity(this.coordinates.size() + coordinates.length);
		Collections.addAll(this.coordinates, coordinates);
	}

	public ArrayList<Position> getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(ArrayList<Position> coordinates) {
		this.coordinates = coordinates;
	}
}
