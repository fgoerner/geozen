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

import java.util.List;

/**
 * Serializes {@link Geometry} objects to WKT (Well-Known Text) and EWKT (Extended Well-Known Text) format.
 * <p>
 * WKT is a text markup language for representing vector geometry objects as defined by the Open Geospatial Consortium (OGC).
 * EWKT extends WKT by adding support for SRID (Spatial Reference System Identifier).
 * </p>
 */
public class WktSerializer {

    private static final int WGS_84_SRID = 4326;
    private static final int WEB_MERCATOR_SRID = 3857;

    /**
     * Serializes a {@link Geometry} to WKT format.
     *
     * @param geometry The geometry to serialize
     * @return WKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    public String toWkt(Geometry geometry) {
        return toWkt(geometry, false);
    }

    /**
     * Serializes a {@link Geometry} to EWKT format (including SRID).
     *
     * @param geometry The geometry to serialize
     * @return EWKT representation of the geometry
     * @throws WktException if the geometry type is not supported
     */
    public String toEwkt(Geometry geometry) {
        return toWkt(geometry, true);
    }

    /**
     * Serializes a {@link GeometryCollection} to WKT format.
     *
     * @param collection The geometry collection to serialize
     * @return WKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    public String toWkt(GeometryCollection collection) {
        return toWkt(collection, false);
    }

    /**
     * Serializes a {@link GeometryCollection} to EWKT format (including SRID).
     *
     * @param collection The geometry collection to serialize
     * @return EWKT representation of the geometry collection
     * @throws WktException if any geometry type is not supported
     */
    public String toEwkt(GeometryCollection collection) {
        return toWkt(collection, true);
    }

    private String toWkt(Geometry geometry, boolean includeExtended) {
        if (geometry == null) {
            throw new WktException("Geometry cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        if (includeExtended) {
            sb.append("SRID=").append(getSrid(geometry.getCoordinateReferenceSystem())).append(";");
        }

        switch (geometry) {
            case Point point -> sb.append(serializePoint(point));
            case LineString lineString -> sb.append(serializeLineString(lineString));
            case Polygon polygon -> sb.append(serializePolygon(polygon));
            case MultiPoint multiPoint -> sb.append(serializeMultiPoint(multiPoint));
            case MultiLineString multiLineString -> sb.append(serializeMultiLineString(multiLineString));
            case MultiPolygon multiPolygon -> sb.append(serializeMultiPolygon(multiPolygon));
            default -> throw new WktException("Unsupported geometry type: " + geometry.getClass().getName());
        }

        return sb.toString();
    }

    private String toWkt(GeometryCollection collection, boolean includeExtended) {
        if (collection == null) {
            throw new WktException("GeometryCollection cannot be null");
        }

        StringBuilder sb = new StringBuilder();

        if (includeExtended && !collection.getGeometries().isEmpty()) {
            CoordinateReferenceSystem crs = collection.getGeometries().getFirst().getCoordinateReferenceSystem();
            sb.append("SRID=").append(getSrid(crs)).append(";");
        }

        sb.append("GEOMETRYCOLLECTION");

        if (collection.getGeometries().isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            for (int i = 0; i < collection.getGeometries().size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                Geometry geom = collection.getGeometries().get(i);
                sb.append(toWkt(geom, false));
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializePoint(Point point) {
        StringBuilder sb = new StringBuilder("POINT");
        Position pos = point.getCoordinates();

        if (pos == null) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            appendPosition(sb, pos);
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializeLineString(LineString lineString) {
        StringBuilder sb = new StringBuilder("LINESTRING");
        List<Position> coords = lineString.getCoordinates();

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            appendPositionList(sb, coords);
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializePolygon(Polygon polygon) {
        StringBuilder sb = new StringBuilder("POLYGON");
        List<List<Position>> coords = polygon.getCoordinates();

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            for (int i = 0; i < coords.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append("(");
                appendPositionList(sb, coords.get(i));
                sb.append(")");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializeMultiPoint(MultiPoint multiPoint) {
        StringBuilder sb = new StringBuilder("MULTIPOINT");
        List<Position> coords = multiPoint.getCoordinates();

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            for (int i = 0; i < coords.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append("(");
                appendPosition(sb, coords.get(i));
                sb.append(")");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializeMultiLineString(MultiLineString multiLineString) {
        StringBuilder sb = new StringBuilder("MULTILINESTRING");
        List<List<Position>> coords = multiLineString.getCoordinates();

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            for (int i = 0; i < coords.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append("(");
                appendPositionList(sb, coords.get(i));
                sb.append(")");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private String serializeMultiPolygon(MultiPolygon multiPolygon) {
        StringBuilder sb = new StringBuilder("MULTIPOLYGON");
        List<List<List<Position>>> coords = multiPolygon.getCoordinates();

        if (coords == null || coords.isEmpty()) {
            sb.append(" EMPTY");
        } else {
            sb.append(" (");
            for (int i = 0; i < coords.size(); i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append("(");
                List<List<Position>> polygon = coords.get(i);
                for (int j = 0; j < polygon.size(); j++) {
                    if (j > 0) {
                        sb.append(", ");
                    }
                    sb.append("(");
                    appendPositionList(sb, polygon.get(j));
                    sb.append(")");
                }
                sb.append(")");
            }
            sb.append(")");
        }

        return sb.toString();
    }

    private void appendPosition(StringBuilder sb, Position pos) {
        sb.append(pos.getLongitude()).append(" ").append(pos.getLatitude());
        if (pos.getAltitude() != 0.0) {
            sb.append(" ").append(pos.getAltitude());
        }
    }

    private void appendPositionList(StringBuilder sb, List<Position> positions) {
        for (int i = 0; i < positions.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            appendPosition(sb, positions.get(i));
        }
    }

    private int getSrid(CoordinateReferenceSystem crs) {
        return switch (crs) {
            case WGS_84 -> WGS_84_SRID;
            case WEB_MERCATOR -> WEB_MERCATOR_SRID;
        };
    }
}
