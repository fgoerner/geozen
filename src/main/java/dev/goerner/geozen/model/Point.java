package dev.goerner.geozen.model;

public class Point implements Geometry {
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
