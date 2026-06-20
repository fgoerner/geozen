package dev.goerner.geozen.model.multi_geometry

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.Reprojector

/**
 * A [MultiLineString] is a [Geometry] that represents a collection of
 * [LineStrings][dev.goerner.geozen.model.simple_geometry.LineString] in space. It is defined by a
 * list of [LineStrings][dev.goerner.geozen.model.simple_geometry.LineString] and a
 * [CoordinateReferenceSystem].
 *
 * @param coordinates The list of line strings (each represented as a list of positions). The outer
 *   list may be empty, but each inner list must contain at least 2 positions.
 * @param coordinateReferenceSystem The coordinate reference system, defaults to WGS_84
 * @throws IllegalArgumentException if any inner list contains fewer than 2 positions
 */
data class MultiLineString
@JvmOverloads
constructor(
    val coordinates: List<List<Position>>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem =
        CoordinateReferenceSystem.WGS_84,
) : Geometry(coordinateReferenceSystem) {

    init {
        coordinates.forEachIndexed { index, lineString ->
            require(lineString.size >= 2) {
                "Each LineString in MultiLineString must contain at least 2 positions, " +
                    "but LineString at index $index contained ${lineString.size}"
            }
        }
    }

    override fun fastDistanceTo(other: Geometry): Double =
        ApproximateDistanceCalculator.calculate(this, other)

    override fun exactDistanceTo(other: Geometry): Double =
        PreciseDistanceCalculator.calculate(this, other)

    override fun reprojectTo(crs: CoordinateReferenceSystem): Geometry =
        if (coordinateReferenceSystem == crs) this
        else
            MultiLineString(
                coordinates.map { lineString ->
                    lineString.map { Reprojector.reproject(it, coordinateReferenceSystem, crs) }
                },
                crs,
            )
}
