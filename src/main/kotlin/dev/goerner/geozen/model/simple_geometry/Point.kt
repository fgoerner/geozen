package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [Point] is a [Geometry] that represents a single position in space. It is defined by a single
 * [Position] and a [CoordinateReferenceSystem].
 */
data class Point(
    val coordinates: Position,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    constructor(
        longitude: Double,
        latitude: Double,
        altitude: Double = 0.0,
        coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
    ) : this(
        Position(longitude, latitude, altitude),
        coordinateReferenceSystem
    )

    override fun fastDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> ApproximateDistanceCalculator.calculate(this, other)
            is LineString -> ApproximateDistanceCalculator.calculate(this, other)
            else -> throw UnsupportedOperationException("Fast distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }

    override fun exactDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> PreciseDistanceCalculator.calculate(this, other)
            is LineString -> PreciseDistanceCalculator.calculate(this, other)
            else -> throw UnsupportedOperationException("Exact distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }

    val longitude: Double
        get() = this.coordinates.longitude

    val latitude: Double
        get() = this.coordinates.latitude

    val altitude: Double
        get() = this.coordinates.altitude
}
