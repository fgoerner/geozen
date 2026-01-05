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
class WktSerializer {

    companion object {
        private const val WGS_84_SRID = 4326
        private const val WEB_MERCATOR_SRID = 3857
    }

    /**
     * Serializes a [Geometry] to WKT format.
     * 
     * @param geometry The geometry to serialize
     * @return WKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    fun toWkt(geometry: Geometry): String {
        return toWkt(geometry, false)
    }

    /**
     * Serializes a [Geometry] to EWKT format (including SRID).
     * 
     * @param geometry The geometry to serialize
     * @return EWKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    fun toEwkt(geometry: Geometry): String {
        return toWkt(geometry, true)
    }

    /**
     * Serializes a [GeometryCollection] to WKT format.
     * 
     * @param collection The geometry collection to serialize
     * @return WKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    fun toWkt(collection: GeometryCollection): String {
        return toWkt(collection, false)
    }

    /**
     * Serializes a [GeometryCollection] to EWKT format (including SRID).
     * 
     * @param collection The geometry collection to serialize
     * @return EWKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    fun toEwkt(collection: GeometryCollection): String {
        return toWkt(collection, true)
    }

    private fun toWkt(geometry: Geometry, includeExtended: Boolean): String {
        val sb = StringBuilder()

        if (includeExtended) {
            sb.append("SRID=").append(getSrid(geometry.coordinateReferenceSystem)).append(";")
        }

        when (geometry) {
            is Point -> sb.append(serializePoint(geometry))
            is LineString -> sb.append(serializeLineString(geometry))
            is Polygon -> sb.append(serializePolygon(geometry))
            is MultiPoint -> sb.append(serializeMultiPoint(geometry))
            is MultiLineString -> sb.append(serializeMultiLineString(geometry))
            is MultiPolygon -> sb.append(serializeMultiPolygon(geometry))
            is GeometryCollection -> sb.append(toWkt(geometry, includeExtended))
            else -> throw WktException("Unsupported geometry type: " + geometry.javaClass.getName())
        }

        return sb.toString()
    }

    private fun toWkt(collection: GeometryCollection, includeExtended: Boolean): String {

        val sb = StringBuilder()

        if (includeExtended && !collection.geometries.isEmpty()) {
            val crs: CoordinateReferenceSystem = collection.geometries[0].coordinateReferenceSystem
            sb.append("SRID=").append(getSrid(crs)).append(";")
        }

        sb.append("GEOMETRYCOLLECTION")

        if (collection.geometries.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            for (i in collection.geometries.indices) {
                if (i > 0) {
                    sb.append(", ")
                }
                val geom = collection.geometries[i]
                sb.append(toWkt(geom, false))
            }
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializePoint(point: Point): String {
        val sb = StringBuilder("POINT")
        val pos = point.coordinates

        if (pos == null) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            appendPosition(sb, pos)
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializeLineString(lineString: LineString): String {
        val sb = StringBuilder("LINESTRING")
        val coords = lineString.coordinates

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            appendPositionList(sb, coords)
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializePolygon(polygon: Polygon): String {
        val sb = StringBuilder("POLYGON")
        val coords = polygon.getCoordinates()

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            for (i in coords.indices) {
                if (i > 0) {
                    sb.append(", ")
                }
                sb.append("(")
                appendPositionList(sb, coords[i]!!)
                sb.append(")")
            }
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializeMultiPoint(multiPoint: MultiPoint): String {
        val sb = StringBuilder("MULTIPOINT")
        val coords = multiPoint.coordinates

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            for (i in coords.indices) {
                if (i > 0) {
                    sb.append(", ")
                }
                sb.append("(")
                appendPosition(sb, coords.get(i)!!)
                sb.append(")")
            }
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializeMultiLineString(multiLineString: MultiLineString): String {
        val sb = StringBuilder("MULTILINESTRING")
        val coords = multiLineString.coordinates

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            for (i in coords.indices) {
                if (i > 0) {
                    sb.append(", ")
                }
                sb.append("(")
                appendPositionList(sb, coords[i]!!)
                sb.append(")")
            }
            sb.append(")")
        }

        return sb.toString()
    }

    private fun serializeMultiPolygon(multiPolygon: MultiPolygon): String {
        val sb = StringBuilder("MULTIPOLYGON")
        val coords = multiPolygon.coordinates

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY")
        } else {
            sb.append(" (")
            for (i in coords.indices) {
                if (i > 0) {
                    sb.append(", ")
                }
                sb.append("(")
                val polygon = coords[i]
                for (j in polygon.indices) {
                    if (j > 0) {
                        sb.append(", ")
                    }
                    sb.append("(")
                    appendPositionList(sb, polygon[j]!!)
                    sb.append(")")
                }
                sb.append(")")
            }
            sb.append(")")
        }

        return sb.toString()
    }

    private fun appendPosition(sb: StringBuilder, pos: Position) {
        sb.append(pos.longitude).append(" ").append(pos.latitude)
        if (pos.altitude != 0.0) {
            sb.append(" ").append(pos.altitude)
        }
    }

    private fun appendPositionList(sb: StringBuilder, positions: MutableList<Position?>) {
        for (i in positions.indices) {
            if (i > 0) {
                sb.append(", ")
            }
            appendPosition(sb, positions[i]!!)
        }
    }

    private fun getSrid(crs: CoordinateReferenceSystem): Int {
        return when (crs) {
            CoordinateReferenceSystem.WGS_84 -> WGS_84_SRID
            CoordinateReferenceSystem.WEB_MERCATOR -> WEB_MERCATOR_SRID
        }
    }
}
