package dev.goerner.geozen.model;

/**
 * An immutable {@link ImmutablePosition} represents a point in space using Java records.
 * It is defined by a longitude, latitude, and altitude.
 * 
 * This is a modern Java alternative to the mutable {@link Position} class,
 * providing immutability and reduced boilerplate.
 */
public record ImmutablePosition(double longitude, double latitude, double altitude) {
    
    /**
     * Creates a new {@link ImmutablePosition} at the origin with zero altitude.
     */
    public ImmutablePosition() {
        this(0.0, 0.0, 0.0);
    }
    
    /**
     * Creates a new {@link ImmutablePosition} with the given coordinates and zero altitude.
     * 
     * @param longitude The longitude of the position.
     * @param latitude  The latitude of the position.
     */
    public ImmutablePosition(double longitude, double latitude) {
        this(longitude, latitude, 0.0);
    }
    
    /**
     * Creates a new {@link ImmutablePosition} from an existing {@link Position}.
     * 
     * @param position The {@link Position} to convert.
     * @return A new {@link ImmutablePosition} with the same coordinates.
     */
    public static ImmutablePosition from(Position position) {
        return new ImmutablePosition(
            position.getLongitude(), 
            position.getLatitude(), 
            position.getAltitude()
        );
    }
    
    /**
     * Converts this {@link ImmutablePosition} to a mutable {@link Position}.
     * 
     * @return A new {@link Position} with the same coordinates.
     */
    public Position toPosition() {
        return new Position(longitude, latitude, altitude);
    }
}