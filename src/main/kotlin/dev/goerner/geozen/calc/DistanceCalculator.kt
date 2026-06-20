package dev.goerner.geozen.calc

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position

/**
 * A [DistanceCalculator] defines a contract for computing distances between pairs of geometry
 * objects. Implementations may use different algorithms, e.g. Haversine (fast approximation) or
 * Karney's geodesic method (precise).
 *
 * The primary entry points are:
 * - [calculate] (Position, Position) – the raw geodesic primitive used internally
 * - [calculate] (Geometry, Geometry) – the general dispatch point for all geometry pairs
 */
interface DistanceCalculator {

    /**
     * Calculates the geodesic distance between two raw positions.
     *
     * @param p1 the first position
     * @param p2 the second position
     * @return the distance in meters
     */
    fun calculate(p1: Position, p2: Position): Double

    /**
     * Calculates the minimum distance between any two [Geometry] objects.
     *
     * Dispatches to the appropriate typed implementation based on the runtime types of [g1] and
     * [g2]. Argument order is irrelevant – the method is symmetric.
     *
     * @param g1 the first geometry
     * @param g2 the second geometry
     * @return the minimum distance in meters, or 0.0 if the geometries intersect or overlap
     * @throws UnsupportedOperationException if the combination of geometry types is not supported
     */
    fun calculate(g1: Geometry, g2: Geometry): Double
}
