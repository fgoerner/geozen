package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [MultiPolygon] is a [Geometry] that represents a collection of [Polygons][dev.goerner.geozen.model.simple_geometry.Polygon] in space. It is
 * defined by a list of [Polygons][dev.goerner.geozen.model.simple_geometry.Polygon] and a [CoordinateReferenceSystem].
 *
 * @param coordinates The list of polygons (each represented as a list of rings, where each ring is a list of positions).
 *                    The outer list may be empty. Each polygon must have at least 1 ring (exterior ring).
 *                    Each ring must contain at least 4 positions and be closed (first position equals last position).
 * @param coordinateReferenceSystem The coordinate reference system, defaults to WGS_84
 * @throws IllegalArgumentException if any polygon has no rings, if any ring has fewer than 4 positions,
 *                                  or if any ring is not closed (first position != last position)
 */
data class MultiPolygon(
    val coordinates: List<List<List<Position>>>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    init {
        coordinates.forEachIndexed { polygonIndex, polygon ->
            require(polygon.isNotEmpty()) {
                "Each Polygon in MultiPolygon must contain at least 1 ring (exterior ring), " +
                        "but Polygon at index $polygonIndex contained 0"
            }

            polygon.forEachIndexed { ringIndex, ring ->
                require(ring.size >= 4) {
                    val ringType = if (ringIndex == 0) "exterior ring" else "interior ring at index $ringIndex"
                    "Each ring in MultiPolygon must contain at least 4 positions, " +
                            "but Polygon at index $polygonIndex $ringType contained ${ring.size}"
                }

                require(ring.first() == ring.last()) {
                    val ringType = if (ringIndex == 0) "exterior ring" else "interior ring at index $ringIndex"
                    "Each ring in MultiPolygon must be closed (first position must equal last position), " +
                            "but Polygon at index $polygonIndex $ringType is not closed"
                }
            }
        }
    }

    override fun fastDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Fast distance calculation not implemented yet")
    }

    override fun exactDistanceTo(other: Geometry): Double {
        throw UnsupportedOperationException("Exact distance calculation not implemented yet")
    }
}
