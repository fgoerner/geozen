package dev.goerner.geozen.util;

import dev.goerner.geozen.model.*;

/**
 * Utility class demonstrating Java 24 features like exhaustive pattern matching
 * with sealed classes and enhanced switch expressions.
 */
public final class GeometryUtils {
    
    private GeometryUtils() {
        // Utility class - no instantiation
    }
    
    /**
     * Gets a human-readable type name for any geometry.
     * Demonstrates exhaustive pattern matching with sealed classes.
     * 
     * @param geometry The geometry to get the type name for
     * @return A descriptive string for the geometry type
     */
    public static String getGeometryTypeName(Geometry geometry) {
        return switch (geometry) {
            case Point point -> "Point at (" + point.getLongitude() + ", " + point.getLatitude() + ")";
            case LineString lineString -> "LineString with " + lineString.getCoordinates().size() + " points";
            case Polygon polygon -> "Polygon with " + polygon.getCoordinates().size() + " rings";
            case MultiPoint multiPoint -> "MultiPoint with " + multiPoint.getCoordinates().size() + " points";
            case MultiLineString multiLineString -> "MultiLineString with " + multiLineString.getCoordinates().size() + " line strings";
            case MultiPolygon multiPolygon -> "MultiPolygon with " + multiPolygon.getCoordinates().size() + " polygons";
            case GeometryCollection collection -> "GeometryCollection with " + collection.getGeometries().size() + " geometries";
            // No default case needed - exhaustive matching with sealed class
        };
    }
    
    /**
     * Counts the total number of coordinate points in any geometry.
     * Another example of exhaustive pattern matching.
     * 
     * @param geometry The geometry to count points for
     * @return The total number of coordinate points
     */
    public static int countCoordinatePoints(Geometry geometry) {
        return switch (geometry) {
            case Point point -> 1;
            case LineString lineString -> lineString.getCoordinates().size();
            case Polygon polygon -> polygon.getCoordinates().stream()
                    .mapToInt(ring -> ring.size())
                    .sum();
            case MultiPoint multiPoint -> multiPoint.getCoordinates().size();
            case MultiLineString multiLineString -> multiLineString.getCoordinates().stream()
                    .mapToInt(lineString -> lineString.size())
                    .sum();
            case MultiPolygon multiPolygon -> multiPolygon.getCoordinates().stream()
                    .mapToInt(polygon -> polygon.stream()
                            .mapToInt(ring -> ring.size())
                            .sum())
                    .sum();
            case GeometryCollection collection -> collection.getGeometries().stream()
                    .mapToInt(GeometryUtils::countCoordinatePoints)
                    .sum();
            // No default case needed - sealed class ensures exhaustive matching
        };
    }
}