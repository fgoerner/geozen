package dev.goerner.geozen.wkt;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Deserializes WKT (Well-Known Text) and EWKT (Extended Well-Known Text) strings to {@link Geometry} objects.
 * <p>
 * WKT is a text markup language for representing vector geometry objects as defined by the Open Geospatial Consortium (OGC).
 * EWKT extends WKT by adding support for SRID (Spatial Reference System Identifier).
 * </p>
 */
public class WktDeserializer {

	private static final int WGS_84_SRID = 4326;
	private static final int WEB_MERCATOR_SRID = 3857;

	private static final Pattern SRID_PATTERN = Pattern.compile("^SRID=(\\d+);(.*)$", Pattern.CASE_INSENSITIVE);
	private static final Pattern GEOMETRY_TYPE_PATTERN = Pattern.compile("^(\\w+)\\s*(.*)$", Pattern.CASE_INSENSITIVE);

	/**
	 * Deserializes a WKT or EWKT string to a {@link Geometry} object.
	 *
	 * @param wkt The WKT or EWKT string to deserialize
	 * @return The deserialized geometry
	 * @throws WktException if the WKT string is invalid or contains unsupported geometry types
	 */
	public Geometry fromWkt(String wkt) {
		if (wkt == null || wkt.isBlank()) {
			throw new WktException("WKT string cannot be null or empty");
		}

		String trimmed = wkt.trim();
		CoordinateReferenceSystem crs = CoordinateReferenceSystem.WGS_84;

		// Check for SRID prefix (EWKT)
		Matcher sridMatcher = SRID_PATTERN.matcher(trimmed);
		if (sridMatcher.matches()) {
			int srid = Integer.parseInt(sridMatcher.group(1));
			crs = getCrsFromSrid(srid);
			trimmed = sridMatcher.group(2).trim();
		}

		return parseGeometry(trimmed, crs);
	}

	/**
	 * Deserializes a WKT or EWKT string to a {@link GeometryCollection} object.
	 *
	 * @param wkt The WKT or EWKT string representing a geometry collection
	 * @return The deserialized geometry collection
	 * @throws WktException if the WKT string is invalid or is not a GEOMETRYCOLLECTION
	 */
	public GeometryCollection fromWktAsCollection(String wkt) {
		if (wkt == null || wkt.isBlank()) {
			throw new WktException("WKT string cannot be null or empty");
		}

		String trimmed = wkt.trim();
		CoordinateReferenceSystem crs = CoordinateReferenceSystem.WGS_84;

		// Check for SRID prefix (EWKT)
		Matcher sridMatcher = SRID_PATTERN.matcher(trimmed);
		if (sridMatcher.matches()) {
			int srid = Integer.parseInt(sridMatcher.group(1));
			crs = getCrsFromSrid(srid);
			trimmed = sridMatcher.group(2).trim();
		}

		Matcher typeMatcher = GEOMETRY_TYPE_PATTERN.matcher(trimmed);
		if (!typeMatcher.matches()) {
			throw new WktException("Invalid WKT format");
		}

		String type = typeMatcher.group(1).toUpperCase();
		String coordinates = typeMatcher.group(2).trim();

		if (!type.equals("GEOMETRYCOLLECTION")) {
			throw new WktException("Expected GEOMETRYCOLLECTION but got " + type);
		}

		return parseGeometryCollection(coordinates, crs);
	}

	private Geometry parseGeometry(String wkt, CoordinateReferenceSystem crs) {
		Matcher matcher = GEOMETRY_TYPE_PATTERN.matcher(wkt);
		if (!matcher.matches()) {
			throw new WktException("Invalid WKT format");
		}

		String type = matcher.group(1).toUpperCase();
		String coordinates = matcher.group(2).trim();

		return switch (type) {
			case "POINT" -> parsePoint(coordinates, crs);
			case "LINESTRING" -> parseLineString(coordinates, crs);
			case "POLYGON" -> parsePolygon(coordinates, crs);
			case "MULTIPOINT" -> parseMultiPoint(coordinates, crs);
			case "MULTILINESTRING" -> parseMultiLineString(coordinates, crs);
			case "MULTIPOLYGON" -> parseMultiPolygon(coordinates, crs);
			default -> throw new WktException("Unsupported geometry type: " + type);
		};
	}

	private Point parsePoint(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new Point(new Position(0, 0), crs);
		}

		coords = stripParentheses(coords);
		Position pos = parsePosition(coords);
		return new Point(pos, crs);
	}

	private LineString parseLineString(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new LineString(List.of(), crs);
		}

		coords = stripParentheses(coords);
		List<Position> positions = parsePositionList(coords);
		return new LineString(positions, crs);
	}

	private Polygon parsePolygon(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new Polygon(List.of(), crs);
		}

		coords = stripParentheses(coords);
		List<List<Position>> rings = parseRings(coords);
		return new Polygon(rings, crs);
	}

	private MultiPoint parseMultiPoint(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new MultiPoint(List.of(), crs);
		}

		coords = stripParentheses(coords);
		List<Position> positions = parseMultiPointPositions(coords);
		return new MultiPoint(positions, crs);
	}

	private MultiLineString parseMultiLineString(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new MultiLineString(List.of(), crs);
		}

		coords = stripParentheses(coords);
		List<List<Position>> lineStrings = parseMultiLineStringCoordinates(coords);
		return new MultiLineString(lineStrings, crs);
	}

	private MultiPolygon parseMultiPolygon(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new MultiPolygon(List.of(), crs);
		}

		coords = stripParentheses(coords);
		List<List<List<Position>>> polygons = parseMultiPolygonCoordinates(coords);
		return new MultiPolygon(polygons, crs);
	}

	private GeometryCollection parseGeometryCollection(String coords, CoordinateReferenceSystem crs) {
		if (coords.equalsIgnoreCase("EMPTY")) {
			return new GeometryCollection(List.of());
		}

		coords = stripParentheses(coords);
		List<Geometry> geometries = new ArrayList<>();

		int depth = 0;
		int start = 0;

		for (int i = 0; i < coords.length(); i++) {
			char c = coords.charAt(i);
			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
			} else if (c == ',' && depth == 0) {
				String geomWkt = coords.substring(start, i).trim();
				geometries.add(parseGeometry(geomWkt, crs));
				start = i + 1;
			}
		}

		// Add the last geometry
		if (start < coords.length()) {
			String geomWkt = coords.substring(start).trim();
			geometries.add(parseGeometry(geomWkt, crs));
		}

		return new GeometryCollection(geometries);
	}

	private Position parsePosition(String coords) {
		String[] parts = coords.trim().split("\\s+");
		if (parts.length < 2) {
			throw new WktException("Invalid position format: " + coords);
		}

		try {
			double lon = Double.parseDouble(parts[0]);
			double lat = Double.parseDouble(parts[1]);

			if (parts.length >= 3) {
				double alt = Double.parseDouble(parts[2]);
				return new Position(lon, lat, alt);
			}

			return new Position(lon, lat);
		} catch (NumberFormatException e) {
			throw new WktException("Invalid coordinate value in: " + coords, e);
		}
	}

	private List<Position> parsePositionList(String coords) {
		List<Position> positions = new ArrayList<>();
		String[] coordPairs = coords.split(",");

		for (String coordPair : coordPairs) {
			positions.add(parsePosition(coordPair.trim()));
		}

		return positions;
	}

	private List<Position> parseMultiPointPositions(String coords) {
		List<Position> positions = new ArrayList<>();
		List<String> parts = splitByCommaRespectingParentheses(coords);

		for (String part : parts) {
			String stripped = stripParentheses(part.trim());
			positions.add(parsePosition(stripped));
		}

		return positions;
	}

	private List<List<Position>> parseRings(String coords) {
		List<List<Position>> rings = new ArrayList<>();
		List<String> ringStrings = splitByCommaRespectingParentheses(coords);

		for (String ringString : ringStrings) {
			String stripped = stripParentheses(ringString.trim());
			rings.add(parsePositionList(stripped));
		}

		return rings;
	}

	private List<List<Position>> parseMultiLineStringCoordinates(String coords) {
		List<List<Position>> lineStrings = new ArrayList<>();
		List<String> lineStringParts = splitByCommaRespectingParentheses(coords);

		for (String part : lineStringParts) {
			String stripped = stripParentheses(part.trim());
			lineStrings.add(parsePositionList(stripped));
		}

		return lineStrings;
	}

	private List<List<List<Position>>> parseMultiPolygonCoordinates(String coords) {
		List<List<List<Position>>> polygons = new ArrayList<>();
		List<String> polygonParts = splitByCommaRespectingParentheses(coords);

		for (String part : polygonParts) {
			String stripped = stripParentheses(part.trim());
			polygons.add(parseRings(stripped));
		}

		return polygons;
	}

	private List<String> splitByCommaRespectingParentheses(String input) {
		List<String> result = new ArrayList<>();
		int depth = 0;
		int start = 0;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c == '(') {
				depth++;
			} else if (c == ')') {
				depth--;
			} else if (c == ',' && depth == 0) {
				result.add(input.substring(start, i));
				start = i + 1;
			}
		}

		// Add the last part
		if (start < input.length()) {
			result.add(input.substring(start));
		}

		return result;
	}

	private String stripParentheses(String input) {
		String trimmed = input.trim();
		if (trimmed.startsWith("(") && trimmed.endsWith(")")) {
			return trimmed.substring(1, trimmed.length() - 1).trim();
		}
		return trimmed;
	}

	private CoordinateReferenceSystem getCrsFromSrid(int srid) {
		return switch (srid) {
			case WGS_84_SRID -> CoordinateReferenceSystem.WGS_84;
			case WEB_MERCATOR_SRID -> CoordinateReferenceSystem.WEB_MERCATOR;
			default -> throw new WktException("Unsupported SRID: " + srid);
		};
	}
}
