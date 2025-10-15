package dev.goerner.geozen.model;

/**
 * A {@link Position} represents a point in space. It is defined by a longitude, latitude, and altitude.
 */
public class Position {
    private final double longitude;
    private final double latitude;
    private final double altitude;

    public Position(double longitude, double latitude) {
        this(longitude, latitude, 0);
    }

    public Position(double longitude, double latitude, double altitude) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.altitude = altitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getAltitude() {
        return altitude;
    }
}
