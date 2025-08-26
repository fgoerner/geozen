package dev.goerner.geozen.model.simple_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link Polygon} is a {@link Geometry} that represents an area in space. It is defined by a list of
 * <a href="https://datatracker.ietf.org/doc/html/rfc7946#section-3.1.6">linear rings</a> and a
 * {@link CoordinateReferenceSystem}.
 * <p>
 * The first ring is the exterior ring, defining the outer boundary of the polygon. Any subsequent rings are interior
 * rings, defining holes within the polygon.
 */
public class Polygon extends Geometry {

	private final List<List<Position>> coordinates;

	/**
	 * Creates a new {@link Polygon} with the given exterior and interior rings and the default WGS 84
	 * {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates A list of exterior and interior rings of the {@link Polygon}.
	 */
	public Polygon(List<List<Position>> coordinates) {
		this(coordinates, CoordinateReferenceSystem.WGS_84);
	}

	/**
	 * Creates a new {@link Polygon} with the given exterior and interior rings and the given {@link CoordinateReferenceSystem}.
	 *
	 * @param coordinates               A list of exterior and interior rings of the {@link Polygon}.
	 * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Polygon}.
	 */
	public Polygon(List<List<Position>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
		super(coordinateReferenceSystem);
        List<List<Position>> coordsCopy = new ArrayList<>();
        for (List<Position> ring : coordinates) {
            coordsCopy.add(List.copyOf(ring));
        }
		this.coordinates = List.copyOf(coordsCopy);
	}

	public List<Position> getExteriorRing() {
		if (this.coordinates.isEmpty()) {
			return null;
		}
		return this.coordinates.getFirst();
	}

	public List<List<Position>> getCoordinates() {
		return this.coordinates;
	}
}
