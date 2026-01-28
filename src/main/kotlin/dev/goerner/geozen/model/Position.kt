package dev.goerner.geozen.model

/**
 * A [Position] represents a point in space. It is defined by a longitude, latitude, and altitude.
 *
 * @param longitude The longitude in degrees, must be in the range [-180, 180]
 * @param latitude The latitude in degrees, must be in the range [-90, 90]
 * @param altitude The altitude in meters, defaults to 0.0
 * @throws IllegalArgumentException if longitude, latitude, or altitude are not finite or out of valid range
 */
data class Position(val longitude: Double, val latitude: Double, val altitude: Double = 0.0) {
    init {
        require(longitude.isFinite()) { "Longitude must be a finite number, but was $longitude" }
        require(latitude.isFinite()) { "Latitude must be a finite number, but was $latitude" }
        require(altitude.isFinite()) { "Altitude must be a finite number, but was $altitude" }
        require(longitude in -180.0..180.0) { "Longitude must be in range [-180, 180], but was $longitude" }
        require(latitude in -90.0..90.0) { "Latitude must be in range [-90, 90], but was $latitude" }
    }
}
