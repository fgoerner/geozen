package dev.goerner.geozen.model

/**
 * A [Position] represents a point in space. It is defined by a longitude, latitude, and altitude.
 */
class Position(val longitude: Double, val latitude: Double, val altitude: Double = 0.0)
