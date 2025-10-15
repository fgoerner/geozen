package dev.goerner.geozen.model.simple_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;

/**
 * A {@link Point} is a {@link Geometry} that represents a single position in space. It is defined by a single
 * {@link Position} and a {@link CoordinateReferenceSystem}.
 */
public class Point extends Geometry {

    private final Position coordinates;

    /**
     * Creates a new {@link Point} at the given coordinates with the default WGS 84 {@link CoordinateReferenceSystem}.
     *
     * @param longitude The longitude of the {@link Point}.
     * @param latitude  The latitude of the {@link Point}.
     */
    public Point(double longitude, double latitude) {
        this(longitude, latitude, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link Point} at the given coordinates with the default WGS 84 {@link CoordinateReferenceSystem}.
     *
     * @param longitude The longitude of the {@link Point}.
     * @param latitude  The latitude of the {@link Point}.
     * @param altitude  The altitude of the {@link Point}.
     */
    public Point(double longitude, double latitude, double altitude) {
        this(longitude, latitude, altitude, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link Point} at the given {@link Position} with the default WGS 84 {@link CoordinateReferenceSystem}.
     *
     * @param coordinates The {@link Position} of the {@link Point}.
     */
    public Point(Position coordinates) {
        this(coordinates, CoordinateReferenceSystem.WGS_84);
    }

    /**
     * Creates a new {@link Point} at the given coordinates with the given {@link CoordinateReferenceSystem}.
     *
     * @param longitude                 The longitude of the {@link Point}.
     * @param latitude                  The latitude of the {@link Point}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
     */
    public Point(double longitude, double latitude, CoordinateReferenceSystem coordinateReferenceSystem) {
        this(longitude, latitude, 0, coordinateReferenceSystem);
    }

    /**
     * Creates a new {@link Point} at the given coordinates with the given {@link CoordinateReferenceSystem}.
     *
     * @param longitude                 The longitude of the {@link Point}.
     * @param latitude                  The latitude of the {@link Point}.
     * @param altitude                  The altitude of the {@link Point}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
     */
    public Point(double longitude, double latitude, double altitude, CoordinateReferenceSystem coordinateReferenceSystem) {
        this(new Position(longitude, latitude, altitude), coordinateReferenceSystem);
    }

    /**
     * Creates a new {@link Point} at the given {@link Position} with the given {@link CoordinateReferenceSystem}.
     *
     * @param coordinates               The {@link Position} of the {@link Point}.
     * @param coordinateReferenceSystem The {@link CoordinateReferenceSystem} of the {@link Point}.
     */
    public Point(Position coordinates, CoordinateReferenceSystem coordinateReferenceSystem) {
        super(coordinateReferenceSystem);
        this.coordinates = coordinates;
    }

    public double getLongitude() {
        return this.coordinates.getLongitude();
    }

    public double getLatitude() {
        return this.coordinates.getLatitude();
    }

    public double getAltitude() {
        return this.coordinates.getAltitude();
    }

    public Position getCoordinates() {
        return this.coordinates;
    }
}
