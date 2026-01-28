package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [LineString] is a [Geometry] that represents a sequence of [Positions][Position] in space. It is
 * defined by a list of [Positions][Position] and a [CoordinateReferenceSystem].
 *
 * @param coordinates The list of positions, must contain at least 2 positions
 * @param coordinateReferenceSystem The coordinate reference system, defaults to WGS_84
 * @throws IllegalArgumentException if coordinates contains fewer than 2 positions
 */
data class LineString(
    val coordinates: List<Position>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    init {
        require(coordinates.size >= 2) {
            "LineString must contain at least 2 positions, but contained ${coordinates.size}"
        }
    }

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
