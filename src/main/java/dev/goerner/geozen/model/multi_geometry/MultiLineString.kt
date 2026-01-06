package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link MultiLineString} is a {@link Geometry} that represents a collection of {@link LineString LineStrings} in
 * space. It is defined by a list of {@link LineString LineStrings} and a {@link CoordinateReferenceSystem}.
 */
public class MultiLineString extends Geometry {

    private final List<List<Position>> coordinates;

    /**
     * Creates a new {@link MultiLineString} with the given list of {@link LineString LineStrings} and the default WGS 84
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates The list of {@link LineString LineStrings} representing the {@link MultiLineString}.
     */
    public MultiLineString(List<List<Position>> coordinates) {
        this(coordinates, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link MultiLineString} with the given list of {@link LineString LineStrings} and the given
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates               The list of {@link LineString LineStrings} representing the {@link MultiLineString}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiLineString}.
     */
    public MultiLineString(List<List<Position>> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
        super(coordinateReferenceSystem);
        List<List<Position>> coordsCopy = new ArrayList<>();
        for (List<Position> line : coordinates) {
            coordsCopy.add(List.copyOf(line));
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

    public List<List<Position>> getCoordinates() {
        return coordinates;
    }
}
