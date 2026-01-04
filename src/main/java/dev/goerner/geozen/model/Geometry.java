package dev.goerner.geozen.model;

/**
 * A {@link Geometry} represents spatial objects in space. It is defined by a {@link CoordinateReferenceSystem} and by
 * some data structure of {@link Position Positions} provided by its descendants, that define the shape of the spatial
 * object.
 */
public abstract class Geometry {
    private final CoordinateReferenceSystem coordinateReferenceSystem;

    public Geometry(CoordinateReferenceSystem coordinateReferenceSystem) {
        this.coordinateReferenceSystem = coordinateReferenceSystem;
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
     * Calculates an approximate distance to another {@link Geometry}.
     *
     * <p>This method is intended to provide a fast approximation of the distance between two geometries,
     * which may be less accurate than the exact distance calculation. The specific algorithm used for
     * this approximation is left to the implementation in the subclasses of {@link Geometry}.</p>
     *
     * @param other The other {@link Geometry} to which the distance is calculated.
     * @return An approximate distance to the other {@link Geometry}.
     */
    public abstract double getFastDistanceTo(Geometry other);

    /**
     * Calculates the exact distance to another {@link Geometry}.
     *
     * <p>This method is intended to provide an accurate calculation of the distance between two geometries.
     * The specific algorithm used for this exact distance calculation is left to the implementation in
     * the subclasses of {@link Geometry}.</p>
     *
     * @param other The other {@link Geometry} to which the distance is calculated.
     * @return The exact distance to the other {@link Geometry}.
     */
    public abstract double getExactDistanceTo(Geometry other);
}
