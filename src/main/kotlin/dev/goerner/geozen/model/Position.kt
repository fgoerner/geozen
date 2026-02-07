package dev.goerner.geozen.model

/**
 * A [Position] represents a point in space. It is defined by a longitude, latitude, and altitude.
 *
 * @param longitude The longitude in the unit of the [CoordinateReferenceSystem] of its containing geometry
 * @param latitude The latitude in the unit of the [CoordinateReferenceSystem] of its containing geometry
 * @param altitude The altitude in meters, defaults to 0.0
 * @throws IllegalArgumentException if longitude, latitude, or altitude are not finite
 */
data class Position(val longitude: Double, val latitude: Double, val altitude: Double = 0.0) {
    init {
        require(longitude.isFinite()) { "Longitude must be a finite number, but was $longitude" }
        require(latitude.isFinite()) { "Latitude must be a finite number, but was $latitude" }
        require(altitude.isFinite()) { "Altitude must be a finite number, but was $altitude" }
    }
}
