package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.Polygon;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link MultiPolygon} is a {@link Geometry} that represents a collection of {@link Polygon Polygons} in space. It is
 * defined by a list of {@link Polygon Polygons} and a {@link CoordinateReferenceSystem}.
 */
public class MultiPolygon extends Geometry {

    private final List<List<List<Position>>> coordinates;

    /**
     * Creates a new {@link MultiPolygon} with the given list of {@link Polygon Polygons} and the default WGS 84
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates The list of {@link Polygon Polygons} representing the {@link MultiPolygon}.
     */
    public MultiPolygon(List<List<List<Position>>> coordinates) {
        this(coordinates, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link MultiPolygon} with the given list of {@link Polygon Polygons} and the given
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates               The list of {@link Polygon Polygons} representing the {@link MultiPolygon}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiPolygon}.
     */
    public MultiPolygon(List<List<List<Position>>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
        super(coordinateReferenceSystem);
        List<List<List<Position>>> coordsCopy = new ArrayList<>();
        for (List<List<Position>> polygon : coordinates) {
            List<List<Position>> polygonCopy = new ArrayList<>();
            for (List<Position> ring : polygon) {
                polygonCopy.add(List.copyOf(ring));
            }
            coordsCopy.add(List.copyOf(polygonCopy));
        }
        this.coordinates = List.copyOf(coordsCopy);
    }

    @Override
    public double getFastDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Fast distance calculation not implemented yet");
    }

    @Override
    public double getExactDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Exact distance calculation not implemented yet");
    }

    public List<List<List<Position>>> getCoordinates() {
        return coordinates;
    }
}
