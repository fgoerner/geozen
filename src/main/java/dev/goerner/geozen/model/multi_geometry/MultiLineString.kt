package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [MultiLineString] is a [Geometry] that represents a collection of [LineStrings][dev.goerner.geozen.model.simple_geometry.LineString] in
 * space. It is defined by a list of [LineStrings][dev.goerner.geozen.model.simple_geometry.LineString] and a [CoordinateReferenceSystem].
 */
class MultiLineString(
    val coordinates: List<List<Position>>,
    coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun getFastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun getExactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }
}
