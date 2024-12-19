package dev.goerner.geozen.model;

public class MultiLineString implements Geometry {
	private Position[][] coordinates;

	public MultiLineString(Position[][] coordinates) {
		this.coordinates = coordinates;
	}

	public Position[][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position[][] coordinates) {
		this.coordinates = coordinates;
	}
}
