package dev.goerner.geozen.wkt

/**
 * Exception thrown when an error occurs during WKT/EWKT serialization or deserialization.
 */
class WktException : RuntimeException {
    constructor(message: String?) : super(message)

    constructor(message: String?, cause: Throwable?) : super(message, cause)
}

