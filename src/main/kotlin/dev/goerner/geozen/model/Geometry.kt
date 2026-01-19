package dev.goerner.geozen.model

/**
 * A [Geometry] represents spatial objects in space. It is defined by a [CoordinateReferenceSystem] and by
 * some data structure of [Positions][Position] provided by its descendants, that define the shape of the spatial
 * object.
 */
abstract class Geometry(open val coordinateReferenceSystem: CoordinateReferenceSystem) {

    /**
     * Calculates an approximate distance to another [Geometry].
     * 
     * 
     * This method is intended to provide a fast approximation of the distance between two geometries,
     * which may be less accurate than the exact distance calculation. The specific algorithm used for
     * this approximation is left to the implementation in the subclasses of [Geometry].
     * 
     * @param other The other [Geometry] to which the distance is calculated.
     * @return An approximate distance to the other [Geometry].
     */
    abstract fun fastDistanceTo(other: Geometry): Double

    /**
     * Calculates the exact distance to another [Geometry].
     * 
     * 
     * This method is intended to provide an accurate calculation of the distance between two geometries.
     * The specific algorithm used for this exact distance calculation is left to the implementation in
     * the subclasses of [Geometry].
     * 
     * @param other The other [Geometry] to which the distance is calculated.
     * @return The exact distance to the other [Geometry].
     */
    abstract fun exactDistanceTo(other: Geometry): Double
}
