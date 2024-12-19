package dev.goerner.geozen.model;

public class MultiPolygon implements Geometry {
	private Position[][][] coordinates;

	public MultiPolygon(Position[][][] coordinates) {
		this.coordinates = coordinates;
	}

	public Position[][][] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Position[][][] coordinates) {
		this.coordinates = coordinates;
	}
}
