package dev.goerner.geozen.model.collections

import dev.goerner.geozen.calc.ApproximateDistanceCalculator
import dev.goerner.geozen.calc.PreciseDistanceCalculator
import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry

/**
 * A [GeometryCollection] is a collection of [Geometries][Geometry].
 */
data class GeometryCollection(
    val geometries: List<Geometry>,
    override val coordinateReferenceSystem: CoordinateReferenceSystem = CoordinateReferenceSystem.WGS_84
) : Geometry(coordinateReferenceSystem) {

    init {
        geometries.forEachIndexed { index, geometry ->
            require(geometry.coordinateReferenceSystem == coordinateReferenceSystem) {
                "The coordinate reference system of each geometry (${geometry.coordinateReferenceSystem}) " +
                        "must match the coordinate reference system of the geometry collection ($coordinateReferenceSystem), " +
                        "but geometry at index $index had a different coordinate reference system"
            }
        }
    }

    override fun fastDistanceTo(other: Geometry): Double =
        ApproximateDistanceCalculator.calculate(this, other)

    override fun exactDistanceTo(other: Geometry): Double =
        PreciseDistanceCalculator.calculate(this, other)

    override fun reprojectTo(crs: CoordinateReferenceSystem): Geometry =
        if (coordinateReferenceSystem == crs) this
        else GeometryCollection(
            geometries.map { it.reprojectTo(crs) },
            crs
        )
}
