package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry

/**
 * A [GeometryCollection] is a collection of [Geometries][Geometry].
 */
data class GeometryCollection(
    val geometries: List<Geometry>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun getFastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun getExactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }
}
