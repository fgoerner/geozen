package dev.goerner.geozen.model.collections;

import dev.goerner.geozen.model.Geometry;

import java.util.List;

/**
 * A {@link GeometryCollection} is a collection of {@link Geometry Geometries}.
 */
public class GeometryCollection {

    private final List<Geometry> geometries;

    /**
     * Creates a new {@link GeometryCollection} with the given list of {@link Geometry Geometries}.
     *
     * @param geometries The list of {@link Geometry Geometries} representing the {@link GeometryCollection}.
     */
    public GeometryCollection(List<Geometry> geometries) {
        this.geometries = List.copyOf(geometries);
    }

    public List<Geometry> getGeometries() {
        return geometries;
    }
}
