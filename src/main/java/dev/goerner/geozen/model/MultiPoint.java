package dev.goerner.geozen.model;

public class MultiPoint implements Geometry{
	private Position[] coordinates;

	public MultiPoint(Position[] coordinates) {
		this.coordinates = coordinates;
	}

	public Position[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position[] coordinates) {
		this.coordinates = coordinates;
	}
}
