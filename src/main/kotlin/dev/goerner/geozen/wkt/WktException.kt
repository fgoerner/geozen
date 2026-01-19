package dev.goerner.geozen.wkt

/**
 * Exception thrown when an error occurs during WKT/EWKT serialization or deserialization.
 */
class WktException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

