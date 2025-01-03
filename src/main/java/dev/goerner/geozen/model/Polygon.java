package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link Polygon} is a {@link Geometry} that represents an area in space. It is defined by a list of
 * <a href="https://datatracker.ietf.org/doc/html/rfc7946#section-3.1.6">linear rings</a> and a
 * {@link CoordinateReferenceSystem}.
 * <p>
 * The first ring is the exterior ring, defining the outer boundary of the polygon. Any subsequent rings are interior
 * rings, defining holes within the polygon.
 */
public class Polygon extends Geometry {

	private ArrayList<ArrayList<Position>> coordinates;

	/**
	 * Creates a new empty {@link Polygon} with the default WGS 84 {@link CoordinateReferenceSystem}.
	 */
	public Polygon() {
		this(CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Polygon} with the given exterior and interior rings and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates A list of exterior and interior rings of the {@link Polygon}.
	 */
	public Polygon(ArrayList<ArrayList<Position>> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Polygon} with the given {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Polygon}.
	 */
	public Polygon(CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = new ArrayList<>();
	}

	/**
	 * Creates a new {@link Polygon} with the given exterior and interior rings and the given {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               A list of exterior and interior rings of the {@link Polygon}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Polygon}.
	 */
	public Polygon(ArrayList<ArrayList<Position>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = coordinates;
	}

	public ArrayList<Position> getExteriorRing() {
		if (this.coordinates.isEmpty()) {
			return null;
		}
		return this.coordinates.getFirst();
	}

	public void setExteriorRing(ArrayList<Position> exteriorRing) {
		if (this.coordinates.isEmpty()) {
			this.coordinates.add(exteriorRing);
		} else {
			this.coordinates.set(0, exteriorRing);
		}
	}

	public ArrayList<ArrayList<Position>> getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(ArrayList<ArrayList<Position>> coordinates) {
		this.coordinates = coordinates;
	}
}
