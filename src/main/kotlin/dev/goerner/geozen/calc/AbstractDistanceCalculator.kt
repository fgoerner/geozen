package dev.goerner.geozen.calc

import dev.goerner.geozen.model.CoordinateReferenceSystem
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon

/**
 * Abstract base class for distance calculators.
 *
 * Implements [calculate] (Geometry, Geometry) as the single public dispatch point. It normalises
 * the argument order and delegates to one of 21 private typed overloads, each of which ultimately
 * calls [calculate] (Position, Position) or [calculateMinDistanceToPositions].
 *
 * Subclasses only need to provide:
 * - [calculate] (Position, Position) – the geodesic primitive (Haversine or Karney)
 * - [calculateMinDistanceToPositions] – the point-to-polyline primitive
 *
 * Adding a new geometry type requires:
 * 1. New typed overloads in this class for each combination with existing types.
 * 2. New branches in [calculate] (Geometry, Geometry) and [geometryOrdinal].
 */
abstract class AbstractDistanceCalculator : DistanceCalculator {

    /**
     * Dispatches a distance computation between any two [Geometry] objects.
     *
     * Argument order is normalised so the type with the lower [geometryOrdinal] always appears
     * first, then the appropriate private typed overload is selected via a 'when' expression.
     *
     * @param g1 the first geometry
     * @param g2 the second geometry
     * @return the minimum distance in meters, or 0.0 if the geometries intersect / overlap
     * @throws UnsupportedOperationException if the geometry type combination is unsupported
     */
    override fun calculate(g1: Geometry, g2: Geometry): Double {
        val r1 = g1.reprojectTo(CoordinateReferenceSystem.WGS_84)
        val r2 = g2.reprojectTo(CoordinateReferenceSystem.WGS_84)
        val (a, b) = if (geometryOrdinal(r1) <= geometryOrdinal(r2)) r1 to r2 else r2 to r1
        return when (a) {
            is Point if b is Point -> calculate(a, b)
            is Point if b is LineString -> calculate(a, b)
            is Point if b is Polygon -> calculate(a, b)
            is Point if b is MultiPoint -> calculate(a, b)
            is Point if b is MultiLineString -> calculate(a, b)
            is Point if b is MultiPolygon -> calculate(a, b)
            is Point if b is GeometryCollection -> calculate(a, b)
            is LineString if b is LineString -> calculate(a, b)
            is LineString if b is Polygon -> calculate(a, b)
            is LineString if b is MultiPoint -> calculate(a, b)
            is LineString if b is MultiLineString -> calculate(a, b)
            is LineString if b is MultiPolygon -> calculate(a, b)
            is LineString if b is GeometryCollection -> calculate(a, b)
            is Polygon if b is Polygon -> calculate(a, b)
            is Polygon if b is MultiPoint -> calculate(a, b)
            is Polygon if b is MultiLineString -> calculate(a, b)
            is Polygon if b is MultiPolygon -> calculate(a, b)
            is Polygon if b is GeometryCollection -> calculate(a, b)
            is MultiPoint if b is MultiPoint -> calculate(a, b)
            is MultiPoint if b is MultiLineString -> calculate(a, b)
            is MultiPoint if b is MultiPolygon -> calculate(a, b)
            is MultiPoint if b is GeometryCollection -> calculate(a, b)
            is MultiLineString if b is MultiLineString -> calculate(a, b)
            is MultiLineString if b is MultiPolygon -> calculate(a, b)
            is MultiLineString if b is GeometryCollection -> calculate(a, b)
            is MultiPolygon if b is MultiPolygon -> calculate(a, b)
            is MultiPolygon if b is GeometryCollection -> calculate(a, b)
            is GeometryCollection if b is GeometryCollection -> calculate(a, b)
            else ->
                throw UnsupportedOperationException(
                    "Distance calculation is not supported between geometry types: " +
                        "${g1::class.simpleName} and ${g2::class.simpleName}"
                )
        }
    }

    /**
     * Returns an ordinal that defines the canonical argument order used by [calculate] (Geometry,
     * Geometry). Lower ordinal types are always placed first.
     */
    private fun geometryOrdinal(g: Geometry): Int =
        when (g) {
            is Point -> 0
            is LineString -> 1
            is Polygon -> 2
            is MultiPoint -> 3
            is MultiLineString -> 4
            is MultiPolygon -> 5
            is GeometryCollection -> 6
            else -> Int.MAX_VALUE
        }

    /**
     * Calculates the minimum distance from a [Point] to a sequence of [Position]s, treating
     * consecutive pairs as line segments. The algorithm used is implementation-specific.
     *
     * @param p the reference point
     * @param positions the sequence of positions forming a linestring or ring
     * @return the minimum distance in meters
     */
    protected abstract fun calculateMinDistanceToPositions(
        p: Point,
        positions: List<Position>,
    ): Double

    private fun calculate(p1: Point, p2: Point): Double = calculate(p1.coordinates, p2.coordinates)

    private fun calculate(p: Point, lineString: LineString): Double =
        calculateMinDistanceToPositions(p, lineString.coordinates)

    private fun calculate(p: Point, polygon: Polygon): Double {
        val rings = polygon.coordinates
        val exteriorRing = rings[0]
        val interiorRings = rings.drop(1)

        val containmentResult =
            PointToPolygonDistanceHelper.analyzePointPolygonContainment(
                p.longitude,
                p.latitude,
                exteriorRing,
                interiorRings,
            )

        return when (containmentResult) {
            is PointToPolygonDistanceHelper.ContainmentResult.InsidePolygon -> 0.0
            is PointToPolygonDistanceHelper.ContainmentResult.InsideHole ->
                calculateMinDistanceToPositions(p, containmentResult.holeRing)
            is PointToPolygonDistanceHelper.ContainmentResult.OutsidePolygon ->
                calculateMinDistanceToPositions(p, exteriorRing)
        }
    }

    private fun calculate(point: Point, multiPoint: MultiPoint): Double {
        require(multiPoint.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        return multiPoint.coordinates.minOf {
            calculate(point, Point(it, multiPoint.coordinateReferenceSystem))
        }
    }

    private fun calculate(point: Point, multiLineString: MultiLineString): Double {
        require(multiLineString.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        return multiLineString.coordinates.minOf {
            calculate(point, LineString(it, multiLineString.coordinateReferenceSystem))
        }
    }

    private fun calculate(point: Point, multiPolygon: MultiPolygon): Double {
        require(multiPolygon.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiPolygon.coordinates.minOf {
            calculate(point, Polygon(it, multiPolygon.coordinateReferenceSystem))
        }
    }

    private fun calculate(lineString1: LineString, lineString2: LineString): Double {
        val positions1 = lineString1.coordinates
        val positions2 = lineString2.coordinates

        if (GeometricUtils.doPolylineSegmentsIntersect(positions1, positions2)) {
            return 0.0
        }

        return LineStringToLineStringDistanceHelper.calculateBidirectionalDistance(
            positions1,
            positions2,
            ::calculateMinDistanceToPositions,
        )
    }

    private fun calculate(lineString: LineString, polygon: Polygon): Double {
        val lineStringPositions = lineString.coordinates
        val rings = polygon.coordinates
        val exteriorRing = rings[0]
        val interiorRings = rings.drop(1)

        val analysisResult =
            LineStringToPolygonDistanceHelper.analyzeLineStringPolygonRelationship(
                lineStringPositions,
                exteriorRing,
                interiorRings,
            )

        return when (analysisResult) {
            is LineStringToPolygonDistanceHelper.AnalysisResult.Intersection -> 0.0
            is LineStringToPolygonDistanceHelper.AnalysisResult.FullyContained -> 0.0
            is LineStringToPolygonDistanceHelper.AnalysisResult.InHole ->
                LineStringToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    lineStringPositions,
                    analysisResult.holeContainingVertex,
                    ::calculateMinDistanceToPositions,
                )
            is LineStringToPolygonDistanceHelper.AnalysisResult.NoIntersectionOrContainment ->
                LineStringToPolygonDistanceHelper.calculateDistanceForGeneralCase(
                    lineStringPositions,
                    rings,
                    exteriorRing,
                    interiorRings,
                    ::calculateMinDistanceToPositions,
                )
        }
    }

    private fun calculate(polygon1: Polygon, polygon2: Polygon): Double {
        val rings1 = polygon1.coordinates
        val exteriorRing1 = rings1[0]
        val interiorRings1 = rings1.drop(1)

        val rings2 = polygon2.coordinates
        val exteriorRing2 = rings2[0]
        val interiorRings2 = rings2.drop(1)

        val analysisResult =
            PolygonToPolygonDistanceHelper.analyzePolygonPolygonRelationship(
                rings1,
                rings2,
                exteriorRing1,
                interiorRings1,
                exteriorRing2,
                interiorRings2,
            )

        return when (analysisResult) {
            is PolygonToPolygonDistanceHelper.AnalysisResult.Intersection -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon1ContainedInPolygon2 -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon2ContainedInPolygon1 -> 0.0
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon1InHoleOfPolygon2 ->
                PolygonToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    rings1,
                    analysisResult.holeRing,
                    ::calculateMinDistanceToPositions,
                )
            is PolygonToPolygonDistanceHelper.AnalysisResult.Polygon2InHoleOfPolygon1 ->
                PolygonToPolygonDistanceHelper.calculateDistanceForHoleCase(
                    rings2,
                    analysisResult.holeRing,
                    ::calculateMinDistanceToPositions,
                )
            is PolygonToPolygonDistanceHelper.AnalysisResult.NoIntersectionOrContainment ->
                PolygonToPolygonDistanceHelper.calculateDistanceForGeneralCase(
                    rings1,
                    rings2,
                    ::calculateMinDistanceToPositions,
                )
        }
    }

    private fun calculate(lineString: LineString, multiPoint: MultiPoint): Double {
        require(multiPoint.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        return multiPoint.coordinates.minOf {
            calculate(Point(it, multiPoint.coordinateReferenceSystem), lineString)
        }
    }

    private fun calculate(lineString: LineString, multiLineString: MultiLineString): Double {
        require(multiLineString.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        return multiLineString.coordinates.minOf {
            calculate(lineString, LineString(it, multiLineString.coordinateReferenceSystem))
        }
    }

    private fun calculate(lineString: LineString, multiPolygon: MultiPolygon): Double {
        require(multiPolygon.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiPolygon.coordinates.minOf {
            calculate(lineString, Polygon(it, multiPolygon.coordinateReferenceSystem))
        }
    }

    private fun calculate(polygon: Polygon, multiPoint: MultiPoint): Double {
        require(multiPoint.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        return multiPoint.coordinates.minOf {
            calculate(Point(it, multiPoint.coordinateReferenceSystem), polygon)
        }
    }

    private fun calculate(polygon: Polygon, multiLineString: MultiLineString): Double {
        require(multiLineString.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        return multiLineString.coordinates.minOf {
            calculate(LineString(it, multiLineString.coordinateReferenceSystem), polygon)
        }
    }

    private fun calculate(polygon: Polygon, multiPolygon: MultiPolygon): Double {
        require(multiPolygon.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiPolygon.coordinates.minOf {
            calculate(polygon, Polygon(it, multiPolygon.coordinateReferenceSystem))
        }
    }

    private fun calculate(multiPoint1: MultiPoint, multiPoint2: MultiPoint): Double {
        require(multiPoint1.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        require(multiPoint2.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        return multiPoint1.coordinates.minOf {
            calculate(Point(it, multiPoint1.coordinateReferenceSystem), multiPoint2)
        }
    }

    private fun calculate(multiPoint: MultiPoint, multiLineString: MultiLineString): Double {
        require(multiPoint.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        require(multiLineString.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        return multiPoint.coordinates.minOf {
            calculate(Point(it, multiPoint.coordinateReferenceSystem), multiLineString)
        }
    }

    private fun calculate(multiPoint: MultiPoint, multiPolygon: MultiPolygon): Double {
        require(multiPoint.coordinates.isNotEmpty()) {
            "MultiPoint must contain at least one point to calculate distance, but contained 0"
        }
        require(multiPolygon.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiPoint.coordinates.minOf {
            calculate(Point(it, multiPoint.coordinateReferenceSystem), multiPolygon)
        }
    }

    private fun calculate(
        multiLineString1: MultiLineString,
        multiLineString2: MultiLineString,
    ): Double {
        require(multiLineString1.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        require(multiLineString2.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        return multiLineString1.coordinates.minOf {
            calculate(LineString(it, multiLineString1.coordinateReferenceSystem), multiLineString2)
        }
    }

    private fun calculate(multiLineString: MultiLineString, multiPolygon: MultiPolygon): Double {
        require(multiLineString.coordinates.isNotEmpty()) {
            "MultiLineString must contain at least one LineString to calculate distance, but contained 0"
        }
        require(multiPolygon.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiLineString.coordinates.minOf {
            calculate(LineString(it, multiLineString.coordinateReferenceSystem), multiPolygon)
        }
    }

    private fun calculate(multiPolygon1: MultiPolygon, multiPolygon2: MultiPolygon): Double {
        require(multiPolygon1.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        require(multiPolygon2.coordinates.isNotEmpty()) {
            "MultiPolygon must contain at least one Polygon to calculate distance, but contained 0"
        }
        return multiPolygon1.coordinates.minOf {
            calculate(Polygon(it, multiPolygon1.coordinateReferenceSystem), multiPolygon2)
        }
    }

    private fun calculate(g: Geometry, geometryCollection: GeometryCollection): Double {
        require(geometryCollection.geometries.isNotEmpty()) {
            "GeometryCollection must contain at least one geometry to calculate distance, but contained 0"
        }
        return geometryCollection.geometries.minOf { calculate(g, it) }
    }

    private fun calculate(
        geometryCollection1: GeometryCollection,
        geometryCollection2: GeometryCollection,
    ): Double {
        require(geometryCollection1.geometries.isNotEmpty()) {
            "GeometryCollection must contain at least one geometry to calculate distance, but contained 0"
        }
        require(geometryCollection2.geometries.isNotEmpty()) {
            "GeometryCollection must contain at least one geometry to calculate distance, but contained 0"
        }
        return geometryCollection1.geometries.minOf { calculate(it, geometryCollection2) }
    }
}
