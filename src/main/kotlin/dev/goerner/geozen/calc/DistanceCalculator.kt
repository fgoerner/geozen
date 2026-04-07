package dev.goerner.geozen.calc

import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon

/**
 * A [DistanceCalculator] defines a contract for computing distances between pairs of geometry
 * objects. Implementations may use different algorithms, e.g. Haversine (fast approximation)
 * or Karney's geodesic method (precise).
 */
interface DistanceCalculator {

    fun calculate(p1: Point, p2: Point): Double

    fun calculate(p: Point, lineString: LineString): Double

    fun calculate(p: Point, polygon: Polygon): Double

    fun calculate(point: Point, multiPoint: MultiPoint): Double

    fun calculate(point: Point, multiLineString: MultiLineString): Double

    fun calculate(point: Point, multiPolygon: MultiPolygon): Double

    fun calculate(lineString1: LineString, lineString2: LineString): Double

    fun calculate(lineString: LineString, polygon: Polygon): Double

    fun calculate(polygon1: Polygon, polygon2: Polygon): Double

    fun calculate(lineString: LineString, multiPoint: MultiPoint): Double

    fun calculate(lineString: LineString, multiLineString: MultiLineString): Double

    fun calculate(lineString: LineString, multiPolygon: MultiPolygon): Double

    fun calculate(polygon: Polygon, multiPoint: MultiPoint): Double

    fun calculate(polygon: Polygon, multiLineString: MultiLineString): Double

    fun calculate(polygon: Polygon, multiPolygon: MultiPolygon): Double

    fun calculate(multiPoint1: MultiPoint, multiPoint2: MultiPoint): Double

    fun calculate(multiPoint: MultiPoint, multiLineString: MultiLineString): Double

    fun calculate(multiPoint: MultiPoint, multiPolygon: MultiPolygon): Double

    fun calculate(multiLineString1: MultiLineString, multiLineString2: MultiLineString): Double

    fun calculate(multiLineString: MultiLineString, multiPolygon: MultiPolygon): Double

    fun calculate(multiPolygon1: MultiPolygon, multiPolygon2: MultiPolygon): Double
}

