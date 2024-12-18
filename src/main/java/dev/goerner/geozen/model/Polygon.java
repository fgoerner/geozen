package dev.goerner.geozen.model;

public class Polygon extends Geometry {
	private Position[][] coordinates;

	public Polygon(Position[][] coordinates) {
		this.coordinates = coordinates;
	}

	public Position[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position[][] coordinates) {
		this.coordinates = coordinates;
	}
}
