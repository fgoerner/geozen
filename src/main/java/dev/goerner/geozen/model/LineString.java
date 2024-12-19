package dev.goerner.geozen.model;

public class LineString implements Geometry {
	private Position[] coordinates;

	public LineString(Position[] coordinates) {
		this.coordinates = coordinates;
	}

	public Position[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position[] coordinates) {
		this.coordinates = coordinates;
	}
}
