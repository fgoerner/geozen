package dev.goerner.geozen.wkt

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
 * Serializes [Geometry] objects to WKT (Well-Known Text) and EWKT (Extended Well-Known Text) format.
 * 
 * 
 * WKT is a text markup language for representing vector geometry objects as defined by the Open Geospatial Consortium (OGC).
 * EWKT extends WKT by adding support for SRID (Spatial Reference System Identifier).
 * 
 */
object WktSerializer {

    private const val WGS_84_SRID = 4326
    private const val WEB_MERCATOR_SRID = 3857

    /**
     * Serializes a [Geometry] to WKT format.
     *
     * @param geometry The geometry to serialize
     * @return WKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    fun toWkt(geometry: Geometry): String = serialize(geometry, includeExtended = false)

    /**
     * Serializes a [Geometry] to EWKT format (including SRID).
     *
     * @param geometry The geometry to serialize
     * @return EWKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    fun toEwkt(geometry: Geometry): String = serialize(geometry, includeExtended = true)

    /**
     * Serializes a [GeometryCollection] to WKT format.
     *
     * @param collection The geometry collection to serialize
     * @return WKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    fun toWkt(collection: GeometryCollection): String = serialize(collection, includeExtended = false)

    /**
     * Serializes a [GeometryCollection] to EWKT format (including SRID).
     *
     * @param collection The geometry collection to serialize
     * @return EWKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    fun toEwkt(collection: GeometryCollection): String = serialize(collection, includeExtended = true)

    private fun serialize(geometry: Geometry, includeExtended: Boolean): String {
        val wkt = when (geometry) {
            is Point -> serializePoint(geometry)
            is LineString -> serializeLineString(geometry)
            is Polygon -> serializePolygon(geometry)
            is MultiPoint -> serializeMultiPoint(geometry)
            is MultiLineString -> serializeMultiLineString(geometry)
            is MultiPolygon -> serializeMultiPolygon(geometry)
            is GeometryCollection -> serializeGeometryCollection(geometry)
            else -> throw WktException("Unsupported geometry type: ${geometry::class.simpleName}")
        }

        return if (includeExtended) {
            "SRID=${getSrid(geometry.coordinateReferenceSystem)};$wkt"
        } else {
            wkt
        }
    }

    private fun serializePoint(point: Point): String {
        return "POINT (${formatPosition(point.coordinates)})"
    }

    private fun serializeLineString(lineString: LineString): String {
        val coords = lineString.coordinates
        if (coords.isEmpty()) {
            throw WktException("Cannot serialize empty LineString. GeoJSON does not allow empty linestrings.")
        }
        return "LINESTRING (${coords.joinToString(", ") { formatPosition(it) }})"
    }

    private fun serializePolygon(polygon: Polygon): String {
        val coords = polygon.coordinates
        if (coords.isEmpty()) {
            throw WktException("Cannot serialize empty Polygon. GeoJSON does not allow empty polygons.")
        }
        return "POLYGON (${coords.joinToString(", ") { ring ->
            "(${ring.joinToString(", ") { formatPosition(it) }})"
        }})"
    }

    private fun serializeMultiPoint(multiPoint: MultiPoint): String {
        val coords = multiPoint.coordinates
        if (coords.isEmpty()) {
            throw WktException("Cannot serialize empty MultiPoint. GeoJSON does not allow empty multipoints.")
        }
        return "MULTIPOINT (${coords.joinToString(", ") { "(${formatPosition(it)})" }})"
    }

    private fun serializeMultiLineString(multiLineString: MultiLineString): String {
        val coords = multiLineString.coordinates
        if (coords.isEmpty()) {
            throw WktException("Cannot serialize empty MultiLineString. GeoJSON does not allow empty multilinestrings.")
        }
        return "MULTILINESTRING (${coords.joinToString(", ") { line ->
            "(${line.joinToString(", ") { formatPosition(it) }})"
        }})"
    }

    private fun serializeMultiPolygon(multiPolygon: MultiPolygon): String {
        val coords = multiPolygon.coordinates
        if (coords.isEmpty()) {
            throw WktException("Cannot serialize empty MultiPolygon. GeoJSON does not allow empty multipolygons.")
        }
        return "MULTIPOLYGON (${coords.joinToString(", ") { polygon ->
            "(${polygon.joinToString(", ") { ring ->
                "(${ring.joinToString(", ") { formatPosition(it) }})"
            }})"
        }})"
    }

    private fun serializeGeometryCollection(collection: GeometryCollection): String {
        if (collection.geometries.isEmpty()) {
            throw WktException("Cannot serialize empty GeometryCollection. GeoJSON does not allow empty geometry collections.")
        }
        return "GEOMETRYCOLLECTION (${collection.geometries.joinToString(", ") { serialize(it, false) }})"
    }

    private fun formatPosition(pos: Position): String {
        return if (pos.altitude != 0.0) {
            "${pos.longitude} ${pos.latitude} ${pos.altitude}"
        } else {
            "${pos.longitude} ${pos.latitude}"
        }
    }

    private fun getSrid(crs: CoordinateReferenceSystem): Int {
        return when (crs) {
            CoordinateReferenceSystem.WGS_84 -> WGS_84_SRID
            CoordinateReferenceSystem.WEB_MERCATOR -> WEB_MERCATOR_SRID
        }
    }
}
