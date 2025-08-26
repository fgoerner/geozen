package dev.goerner.geozen.model.simple_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;

import java.util.List;

/**
 * A {@link LineString} is a {@link Geometry} that represents a sequence of {@link Position Positions} in space. It is
 * defined by a list of {@link Position Positions} and a {@link CoordinateReferenceSystem}.
 */
public class LineString extends Geometry {

	private final List<Position> coordinates;

	/**
	 * Creates a new {@link LineString} with the given list of {@link Position Positions} and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The list of {@link Position Positions} representing the {@link LineString}.
	 */
	public LineString(List<Position> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link LineString} with the given list of {@link Position Positions} and the given
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The list of {@link Position Positions} representing the {@link LineString}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link LineString}.
	 */
	public LineString(List<Position> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = List.copyOf(coordinates);
	}

	public List<Position> getCoordinates() {
		return this.coordinates;
	}
}
