package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [MultiPoint] is a [Geometry] that represents a collection of [Positions][Position] in space. It is
 * defined by a list of [Positions][Position] and a [CoordinateReferenceSystem].
 */
class MultiPoint(
    val coordinates: List<Position>,
    coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun getFastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun getExactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }
}
