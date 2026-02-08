package dev.goerner.geozen.model.simple_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [Polygon] is a [Geometry] that represents an area in space. It is defined by a list of
 * [linear rings](https://datatracker.ietf.org/doc/html/rfc7946#section-3.1.6) and a
 * [CoordinateReferenceSystem].
 * 
 * 
 * The first ring is the exterior ring, defining the outer boundary of the polygon. Any subsequent rings are interior
 * rings, defining holes within the polygon.
 *
 * @param coordinates The list of linear rings. At least one ring (exterior ring) must be provided.
 *                    Each ring must contain at least 4 positions and be closed (first position equals last position).
 * @param coordinateReferenceSystem The coordinate reference system, defaults to WGS_84
 * @throws IllegalArgumentException if coordinates is empty, if any ring has fewer than 4 positions,
 *                                  or if any ring is not closed (first position != last position)
 */
data class Polygon(
    val coordinates: List<List<Position>>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    init {
        require(coordinates.isNotEmpty()) {
            "Polygon must contain at least 1 ring (exterior ring), but contained 0"
        }

        coordinates.forEachIndexed { index, ring ->
            require(ring.size >= 4) {
                val ringType = if (index == 0) "exterior ring" else "interior ring at index $index"
                "Each ring in Polygon must contain at least 4 positions, " +
                        "but $ringType contained ${ring.size}"
            }

            require(ring.first() == ring.last()) {
                val ringType = if (index == 0) "exterior ring" else "interior ring at index $index"
                "Each ring in Polygon must be closed (first position must equal last position), " +
                        "but $ringType is not closed"
            }
        }
    }

    override fun fastDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> ApproximateDistanceCalculator.calculate(other, this)
            is LineString -> ApproximateDistanceCalculator.calculate(other, this)
            is Polygon -> ApproximateDistanceCalculator.calculate(other, this)
            else -> throw UnsupportedOperationException("Fast distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }

    override fun exactDistanceTo(other: Geometry): Double {
        return when (other) {
            is Point -> PreciseDistanceCalculator.calculate(other, this)
            is LineString -> PreciseDistanceCalculator.calculate(other, this)
            is Polygon -> PreciseDistanceCalculator.calculate(other, this)
            else -> throw UnsupportedOperationException("Exact distance calculation is not supported for geometry type: ${other::class.simpleName}")
        }
    }

    val exteriorRing: List<Position>
        get() {
            if (this.coordinates.isEmpty()) {
                return emptyList()
            }
            return this.coordinates[0]
        }
}
