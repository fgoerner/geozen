package dev.goerner.geozen.model;

/**
 * A {@link Point} is a {@link Geometry} that represents a single position in space. It is defined by a single
 * {@link Position} and a {@link CoordinateReferenceSystem}.
 */
public class Point extends Geometry {

	private Position coordinates;

	/**
	 * Creates a new {@link Point} at the origin coordinates with the default WGS 84 {@link CoordinateReferenceSystem}.
	 */
	public Point() {
		this(0, 0);
	}

	/**
	 * Creates a new {@link Point} at the given coordinates with the default WGS 84 {@link CoordinateReferenceSystem}.
	 *
	 * @param longitude The longitude of the {@link Point}.
	 * @param latitude  The latitude of the {@link Point}.
	 */
	public Point(double longitude, double latitude) {
		this(longitude, latitude, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Point} at the given coordinates with the default WGS 84 {@link CoordinateReferenceSystem}.
	 *
	 * @param longitude The longitude of the {@link Point}.
	 * @param latitude  The latitude of the {@link Point}.
	 * @param altitude  The altitude of the {@link Point}.
	 */
	public Point(double longitude, double latitude, double altitude) {
		this(longitude, latitude, altitude, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Point} at the given {@link Position} with the default WGS 84 {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates The {@link Position} of the {@link Point}.
	 */
	public Point(Position coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Point} at the given coordinates with the given {@link CoordinateReferenceSystem}.
	 *
	 * @param longitude                 The longitude of the {@link Point}.
	 * @param latitude                  The latitude of the {@link Point}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
	 */
	public Point(double longitude, double latitude, CoordinateReferenceSystem coordinateReferenceSystem) {
		this(longitude, latitude, 0, coordinateReferenceSystem);
	}

	/**
	 * Creates a new {@link Point} at the given coordinates with the given {@link CoordinateReferenceSystem}.
	 *
	 * @param longitude                 The longitude of the {@link Point}.
	 * @param latitude                  The latitude of the {@link Point}.
	 * @param altitude                  The altitude of the {@link Point}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
	 */
	public Point(double longitude, double latitude, double altitude, CoordinateReferenceSystem coordinateReferenceSystem) {
		this(new Position(longitude, latitude, altitude), coordinateReferenceSystem);
	}

	/**
	 * Creates a new {@link Point} at the given {@link Position} with the given {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               The {@link Position} of the {@link Point}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
	 */
	public Point(Position coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
		this.coordinates = coordinates;
	}

	public double getLongitude() {
		return this.coordinates.getLongitude();
	}

	public double getLatitude() {
		return this.coordinates.getLatitude();
	}

	public double getAltitude() {
		return this.coordinates.getAltitude();
	}

	public void setLongitude(double longitude) {
		this.coordinates.setLongitude(longitude);
	}

	public void setLatitude(double latitude) {
		this.coordinates.setLatitude(latitude);
	}

	public void setAltitude(double altitude) {
		this.coordinates.setAltitude(altitude);
	}

	public Position getCoordinates() {
		return this.coordinates;
	}

	public void setCoordinates(Position coordinates) {
		this.coordinates = coordinates;
	}
}
