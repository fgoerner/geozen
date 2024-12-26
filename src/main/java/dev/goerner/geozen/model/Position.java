package dev.goerner.geozen.model;

/**
 * A {@link Position} represents a point in space. It is defined by a longitude, latitude, and altitude.
 */
public class Position {
	private double longitude;
	private double latitude;
	private double altitude;

	public Position() {
		this(0, 0);
	}

	public Position(double longitude, double latitude) {
		this(longitude, latitude, 0);
	}

	public Position(double longitude, double latitude, double altitude) {
		this.longitude = longitude;
		this.latitude = latitude;
		this.altitude = altitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}
