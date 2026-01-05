package dev.goerner.geozen.wkt;

/**
 * Exception thrown when an error occurs during WKT/EWKT serialization or deserialization.
 */
public class WktException extends RuntimeException {

    public WktException(String message) {
        super(message);
    }

    public WktException(String message, Throwable cause) {
        super(message, cause);
    }
}

