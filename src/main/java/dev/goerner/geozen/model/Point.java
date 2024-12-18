package dev.goerner.geozen.model;

public class Point extends Geometry {
	private Position coordinates;

	public Point(Position coordinates) {
		this.coordinates = coordinates;
	}

	public Position getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position coordinates) {
		this.coordinates = coordinates;
	}
}
