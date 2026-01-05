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
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Deserializes WKT (Well-Known Text) and EWKT (Extended Well-Known Text) strings to [Geometry] objects.
 * 
 * 
 * WKT is a text markup language for representing vector geometry objects as defined by the Open Geospatial Consortium (OGC).
 * EWKT extends WKT by adding support for SRID (Spatial Reference System Identifier).
 * 
 */
class WktDeserializer {

    companion object {
        private const val WGS_84_SRID = 4326
        private const val WEB_MERCATOR_SRID = 3857

        private val SRID_PATTERN: Pattern = Pattern.compile("^SRID=(\\d+);(.*)$", Pattern.CASE_INSENSITIVE)
        private val GEOMETRY_TYPE_PATTERN: Pattern = Pattern.compile("^(\\w+)\\s*(.*)$", Pattern.CASE_INSENSITIVE)
    }

    /**
     * Deserializes a WKT or EWKT string to a [Geometry] object.
     * 
     * @param wkt The WKT or EWKT string to deserialize
     * @return The deserialized geometry
     * @throws WktException if the WKT string is invalid or contains unsupported geometry types
     */
    fun fromWkt(wkt: String): Geometry {
        if (wkt.isBlank()) {
            throw WktException("WKT string cannot be null or empty")
        }

        var trimmed = wkt.trim()
        var crs = CoordinateReferenceSystem.WGS_84

        // Check for SRID prefix (EWKT)
        val sridMatcher: Matcher = SRID_PATTERN.matcher(trimmed)
        if (sridMatcher.matches()) {
            val srid = Integer.parseInt(sridMatcher.group(1))
            crs = getCrsFromSrid(srid)
            trimmed = sridMatcher.group(2).trim()
        }

        return parseGeometry(trimmed, crs)
    }

    /**
     * Deserializes a WKT or EWKT string to a [GeometryCollection] object.
     * 
     * @param wkt The WKT or EWKT string representing a geometry collection
     * @return The deserialized geometry collection
     * @throws WktException if the WKT string is invalid or is not a GEOMETRYCOLLECTION
     */
    fun fromWktAsCollection(wkt: String): GeometryCollection {
        if (wkt.isBlank()) {
            throw WktException("WKT string cannot be null or empty")
        }

        var trimmed = wkt.trim()
        var crs = CoordinateReferenceSystem.WGS_84

        // Check for SRID prefix (EWKT)
        val sridMatcher: Matcher = SRID_PATTERN.matcher(trimmed)
        if (sridMatcher.matches()) {
            val srid = Integer.parseInt(sridMatcher.group(1))
            crs = getCrsFromSrid(srid)
            trimmed = sridMatcher.group(2).trim()
        }

        val typeMatcher: Matcher = GEOMETRY_TYPE_PATTERN.matcher(trimmed)
        if (!typeMatcher.matches()) {
            throw WktException("Invalid WKT format")
        }

        val type = typeMatcher.group(1).uppercase()
        val coordinates = typeMatcher.group(2).trim()

        if (type != "GEOMETRYCOLLECTION") {
            throw WktException("Expected GEOMETRYCOLLECTION but got $type")
        }

        return parseGeometryCollection(coordinates, crs)
    }

    private fun parseGeometry(wkt: String, crs: CoordinateReferenceSystem): Geometry {
        val matcher: Matcher = GEOMETRY_TYPE_PATTERN.matcher(wkt)
        if (!matcher.matches()) {
            throw WktException("Invalid WKT format")
        }

        val type = matcher.group(1).uppercase()
        val coordinates = matcher.group(2).trim()

        return when (type) {
            "POINT" -> parsePoint(coordinates, crs)
            "LINESTRING" -> parseLineString(coordinates, crs)
            "POLYGON" -> parsePolygon(coordinates, crs)
            "MULTIPOINT" -> parseMultiPoint(coordinates, crs)
            "MULTILINESTRING" -> parseMultiLineString(coordinates, crs)
            "MULTIPOLYGON" -> parseMultiPolygon(coordinates, crs)
            "GEOMETRYCOLLECTION" -> parseGeometryCollection(coordinates, crs)
            else -> throw WktException("Unsupported geometry type: $type")
        }
    }

    private fun parsePoint(coords: String, crs: CoordinateReferenceSystem): Point {
        var coords = coords
        if (coords == "EMPTY") {
            return Point(Position(0.0, 0.0), crs)
        }

        coords = stripParentheses(coords)
        val pos = parsePosition(coords)
        return Point(pos, crs)
    }

    private fun parseLineString(coords: String, crs: CoordinateReferenceSystem): LineString {
        var coords = coords
        if (coords == "EMPTY") {
            return LineString(emptyList(), crs)
        }

        coords = stripParentheses(coords)
        val positions = parsePositionList(coords)
        return LineString(positions, crs)
    }

    private fun parsePolygon(coords: String, crs: CoordinateReferenceSystem): Polygon {
        var coords = coords
        if (coords == "EMPTY") {
            return Polygon(emptyList(), crs)
        }

        coords = stripParentheses(coords)
        val rings = parseRings(coords)
        return Polygon(rings, crs)
    }

    private fun parseMultiPoint(coords: String, crs: CoordinateReferenceSystem): MultiPoint {
        var coords = coords
        if (coords == "EMPTY") {
            return MultiPoint(emptyList(), crs)
        }

        coords = stripParentheses(coords)
        val positions = parseMultiPointPositions(coords)
        return MultiPoint(positions, crs)
    }

    private fun parseMultiLineString(coords: String, crs: CoordinateReferenceSystem): MultiLineString {
        var coords = coords
        if (coords == "EMPTY") {
            return MultiLineString(emptyList(), crs)
        }

        coords = stripParentheses(coords)
        val lineStrings = parseMultiLineStringCoordinates(coords)
        return MultiLineString(lineStrings, crs)
    }

    private fun parseMultiPolygon(coords: String, crs: CoordinateReferenceSystem): MultiPolygon {
        var coords = coords
        if (coords == "EMPTY") {
            return MultiPolygon(emptyList(), crs)
        }

        coords = stripParentheses(coords)
        val polygons = parseMultiPolygonCoordinates(coords)
        return MultiPolygon(polygons, crs)
    }

    private fun parseGeometryCollection(coords: String, crs: CoordinateReferenceSystem): GeometryCollection {
        var coords = coords
        if (coords == "EMPTY") {
            return GeometryCollection(emptyList())
        }

        coords = stripParentheses(coords)
        val geometries: MutableList<Geometry?> = ArrayList()

        var depth = 0
        var start = 0

        for (i in 0..<coords.length) {
            val c: Char = coords[i]
            when (c) {
                '(' -> {
                    depth++
                }

                ')' -> {
                    depth--
                }

                ',' if depth == 0 -> {
                    val geomWkt = coords.substring(start, i).trim()
                    geometries.add(parseGeometry(geomWkt, crs))
                    start = i + 1
                }
            }
        }

        // Add the last geometry
        if (start < coords.length) {
            val geomWkt = coords.substring(start).trim()
            geometries.add(parseGeometry(geomWkt, crs))
        }

        return GeometryCollection(geometries)
    }

    private fun parsePosition(coords: String): Position {
        val parts = coords.trim().split(Regex("\\s+"))
        if (parts.size < 2) {
            throw WktException("Invalid position format: $coords")
        }

        try {
            val lon = parts[0].toDouble()
            val lat = parts[1].toDouble()

            if (parts.size >= 3) {
                val alt = parts[2].toDouble()
                return Position(lon, lat, alt)
            }

            return Position(lon, lat)
        } catch (e: NumberFormatException) {
            throw WktException("Invalid coordinate value in: $coords", e)
        }
    }

    private fun parsePositionList(coords: String): List<Position> {
        return coords.split(",").map { parsePosition(it.trim()) }
    }

    private fun parseMultiPointPositions(coords: String): List<Position> {
        return splitByCommaRespectingParentheses(coords).map {
            val stripped = stripParentheses(it.trim())
            parsePosition(stripped)
        }
    }

    private fun parseRings(coords: String): List<List<Position>> {
        return splitByCommaRespectingParentheses(coords).map {
            val stripped = stripParentheses(it.trim())
            parsePositionList(stripped)
        }
    }

    private fun parseMultiLineStringCoordinates(coords: String): List<List<Position>> {
        return splitByCommaRespectingParentheses(coords).map {
            val stripped = stripParentheses(it.trim())
            parsePositionList(stripped)
        }
    }

    private fun parseMultiPolygonCoordinates(coords: String): List<List<List<Position>>> {
        return splitByCommaRespectingParentheses(coords).map {
            val stripped = stripParentheses(it.trim())
            parseRings(stripped)
        }
    }

    private fun splitByCommaRespectingParentheses(input: String): List<String> {
        val result: MutableList<String> = ArrayList()
        var depth = 0
        var start = 0

        for (i in 0..<input.length) {
            val c: Char = input[i]
            if (c == '(') {
                depth++
            } else if (c == ')') {
                depth--
            } else if (c == ',' && depth == 0) {
                result.add(input.substring(start, i))
                start = i + 1
            }
        }

        // Add the last part
        if (start < input.length) {
            result.add(input.substring(start))
        }

        return result
    }

    private fun stripParentheses(input: String): String {
        val trimmed = input.trim()
        if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
            return trimmed.substring(1, trimmed.length - 1).trim()
        }
        return trimmed
    }

    private fun getCrsFromSrid(srid: Int): CoordinateReferenceSystem {
        return when (srid) {
            WGS_84_SRID -> CoordinateReferenceSystem.WGS_84
            WEB_MERCATOR_SRID -> CoordinateReferenceSystem.WEB_MERCATOR
            else -> throw WktException("Unsupported SRID: $srid")
        }
    }
}
