package dev.goerner.geozen.model.simple_geometry

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
 */
data class Polygon(
    val coordinates: List<List<Position>>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    override fun fastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun exactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }

    val exteriorRing: List<Position>
        get() {
            if (this.coordinates.isEmpty()) {
                return emptyList()
            }
            return this.coordinates[0]
        }
}
