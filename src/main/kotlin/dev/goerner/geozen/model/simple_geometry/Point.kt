package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.Reprojector

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

    override fun fastDistanceTo(other: Geometry): Double =
        ApproximateDistanceCalculator.calculate(this, other)

    override fun exactDistanceTo(other: Geometry): Double =
        PreciseDistanceCalculator.calculate(this, other)

    override fun reprojectTo(crs: CoordinateReferenceSystem): Geometry =
        if (coordinateReferenceSystem == crs) this
        else Point(
            Reprojector.reproject(coordinates, coordinateReferenceSystem, crs),
            crs
        )

    val longitude: Double
        get() = this.coordinates.longitude

    val latitude: Double
        get() = this.coordinates.latitude

    val altitude: Double
        get() = this.coordinates.altitude
}
