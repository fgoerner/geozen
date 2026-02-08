package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.simple_geometry.Point

/**
 * A [MultiPoint] is a [Geometry] that represents a collection of [Positions][Position] in space. It is
 * defined by a list of [Positions][Position] and a [CoordinateReferenceSystem].
 */
data class MultiPoint(
    val coordinates: List<Position>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun fastDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> ApproximateDistanceCalculator.calculate(other, this)
            else -> throw UnsupportedOperationException("Fast distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }

    override fun exactDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> PreciseDistanceCalculator.calculate(other, this)
            else -> throw UnsupportedOperationException("Exact distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }
}
