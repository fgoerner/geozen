package dev.goerner.geozen.model.collections;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;

import java.util.List;

/**
 * A {@link GeometryCollection} is a collection of {@link Geometry Geometries}.
 */
public class GeometryCollection extends Geometry {

    private final List<Geometry> geometries;

    /**
     * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries} and the default WGS 84
     * {@link CoordinateReferenceSystem}.
     *
     * @param geometries The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
     */
    public GeometryCollection(List<Geometry> geometries) {
        this(geometries, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries} and the given {@link CoordinateReferenceSystem}.
     *
     * @param geometries                The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link GeometryCollection}.
     */
    public GeometryCollection(List<Geometry> geometries, CoordinateReferenceSystem coordinateReferenceSystem) {
        super(coordinateReferenceSystem);
        this.geometries = List.copyOf(geometries);
    }

    @Override
    public double getFastDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Fast distance calculation not implemented yet");
    }

    @Override
    public double getExactDistanceTo(Geometry other) {
        throw new UnsupportedOperationException("Exact distance calculation not implemented yet");
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }
}
