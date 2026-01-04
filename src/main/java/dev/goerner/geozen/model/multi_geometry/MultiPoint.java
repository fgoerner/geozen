package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;

import java.util.List;

/**
 * A {@link MultiPoint} is a {@link Geometry} that represents a collection of {@link Position Positions} in space. It is
 * defined by a list of {@link Position Positions} and a {@link CoordinateReferenceSystem}.
 */
public class MultiPoint extends Geometry {

    private final List<Position> coordinates;

    /**
     * Creates a new {@link MultiPoint} with the given list of {@link Position Positions} and the default WGS 84
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates The list of {@link Position Positions} representing the {@link MultiPoint}.
     */
    public MultiPoint(List<Position> coordinates) {
        this(coordinates, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link MultiPoint} with the given list of {@link Position Positions} and the given
     * {@link CoordinateReferenceSystem}.
     *
     * @param coordinates               The list of {@link Position Positions} representing the {@link MultiPoint}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link MultiPoint}.
     */
    public MultiPoint(List<Position> coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
        super(coordinateReferenceSystem);
        this.coordinates = List.copyOf(coordinates);
    }

    @Override
    public double getFastDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Fast distance calculation not implemented yet");
    }

    @Override
    public double getExactDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Exact distance calculation not implemented yet");
    }

    public List<Position> getCoordinates() {
        return coordinates;
    }
}
