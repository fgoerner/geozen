package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;

import java.util.List;

public class ApproximateDistanceCalculator {

    /**
     * Calculates the distance between two geographical positions using the Haversine formula.
     *
     * <p>
     * This method converts the latitude and longitude of the provided {@code Point} objects from
     * degrees to radians, computes the differences in latitude and longitude, and calculates the
     * distance based on the Haversine formula. The Earth's radius is assumed to be 6,371,008.8 meters.
     * </p>
     *
     * @param p1 the first geographical position, with latitude and longitude in degrees
     * @param p2 the second geographical position, with latitude and longitude in degrees
     * @return the computed distance between {@code p1} and {@code p2} in meters
     */
    static double haversineDistance(Position p1, Position p2) {
        final double lat1 = Math.toRadians(p1.getLatitude());
        final double lat2 = Math.toRadians(p2.getLatitude());

        final double lon1 = Math.toRadians(p1.getLongitude());
        final double lon2 = Math.toRadians(p2.getLongitude());

        final double deltaLat = lat2 - lat1;
        final double deltaLon = lon2 - lon1;

        final double distanceFactor = 1.0 - Math.cos(deltaLat) + Math.cos(lat1) * Math.cos(lat2) * (1.0 - Math.cos(deltaLon));

        return 2.0 * 6_371_008.8 * Math.asin(Math.sqrt(distanceFactor / 2.0));
    }

    /**
     * Calculates an approximate geodesic distance between two {@link Point} instances.
     *
     * <p>This method delegates to {@link #haversineDistance(Position, Position)} by using the
     * underlying {@link Position} coordinates of the provided points. It is intended for
     * scenarios where a fast, reasonably accurate distance calculation is sufficient.</p>
     *
     * @param p1 the first point, providing latitude and longitude coordinates
     * @param p2 the second point, providing latitude and longitude coordinates
     * @return the approximate distance between {@code p1} and {@code p2} in meters
     */
    public static double calculate(Point p1, Point p2) {
        return haversineDistance(p1.getCoordinates(), p2.getCoordinates());
    }

    /**
     * Calculates an approximate distance between a Point and a LineString.
     *
     * <p>This method iterates through the segments of the LineString, projects the point onto
     * the segment using a planar approximation (Equirectangular projection), and calculates
     * the Haversine distance to the projected point. This is faster but less accurate than
     * the precise method, especially over large distances or near poles.</p>
     *
     * @param p          the point
     * @param lineString the line string
     * @return the approximate distance in meters
     */
    public static double calculate(Point p, LineString lineString) {
        double minDistance = Double.MAX_VALUE;
        List<Position> positions = lineString.getCoordinates();

        if (positions == null || positions.isEmpty()) {
            return 0.0;
        }

        for (int i = 0; i < positions.size() - 1; i++) {
            Position p1 = positions.get(i);
            Position p2 = positions.get(i + 1);

            // Equirectangular projection approximation for the segment
            double projectionFactor = getSegmentProjectionFactor(p, p1, p2);

            double closestLat, closestLon;

            if (projectionFactor < 0) {
                closestLat = p1.getLatitude();
                closestLon = p1.getLongitude();
            } else if (projectionFactor > 1) {
                closestLat = p2.getLatitude();
                closestLon = p2.getLongitude();
            } else {
                closestLat = p1.getLatitude() + projectionFactor * (p2.getLatitude() - p1.getLatitude());
                closestLon = p1.getLongitude() + projectionFactor * (p2.getLongitude() - p1.getLongitude());
            }

            // Use Haversine for the actual distance measurement to the closest point
            double dist = haversineDistance(p.getCoordinates(), new Position(closestLon, closestLat));
            if (dist < minDistance) {
                minDistance = dist;
            }
        }
        return minDistance;
    }

    private static double getSegmentProjectionFactor(Point p, Position p1, Position p2) {
        double avgLatRad = Math.toRadians((p1.getLatitude() + p.getLatitude()) / 2.0);
        double x = (p.getLongitude() - p1.getLongitude()) * Math.cos(avgLatRad);
        double y = p.getLatitude() - p1.getLatitude();

        double dx = (p2.getLongitude() - p1.getLongitude()) * Math.cos(avgLatRad);
        double dy = p2.getLatitude() - p1.getLatitude();

        double dot = x * dx + y * dy;
        double lenSq = dx * dx + dy * dy;

        return (lenSq != 0) ? dot / lenSq : -1;
    }
}
