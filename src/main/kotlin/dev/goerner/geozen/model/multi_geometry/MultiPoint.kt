package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.Reprojector

/**
 * A [MultiPoint] is a [Geometry] that represents a collection of [Positions][Position] in space. It
 * is defined by a list of [Positions][Position] and a [CoordinateReferenceSystem].
 */
data class MultiPoint
@JvmOverloads
constructor(
    val coordinates: List<Position>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem =
        CoordinateReferenceSystem.WGS_84,
) : Geometry(coordinateReferenceSystem) {

    override fun fastDistanceTo(other: Geometry): Double =
        ApproximateDistanceCalculator.calculate(this, other)

    override fun exactDistanceTo(other: Geometry): Double =
        PreciseDistanceCalculator.calculate(this, other)

    override fun reprojectTo(crs: CoordinateReferenceSystem): Geometry =
        if (coordinateReferenceSystem == crs) this
        else
            MultiPoint(
                coordinates.map { Reprojector.reproject(it, coordinateReferenceSystem, crs) },
                crs,
            )
}
