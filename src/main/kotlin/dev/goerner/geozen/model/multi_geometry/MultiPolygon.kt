package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [MultiPolygon] is a [Geometry] that represents a collection of [Polygons][dev.goerner.geozen.model.simple_geometry.Polygon] in space. It is
 * defined by a list of [Polygons][dev.goerner.geozen.model.simple_geometry.Polygon] and a [CoordinateReferenceSystem].
 */
data class MultiPolygon(
    val coordinates: List<List<List<Position>>>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun getFastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun getExactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }
}
