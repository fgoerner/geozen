package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicMask;

import java.util.List;

public class PreciseDistanceCalculator {

    public static final double WGS84_SEMI_MAJOR_AXIS = 6_378_137.0;

    /**
     * Calculates the distance between two geographical positions using Karney's algorithm.
     *
     * <p>This method utilizes the GeographicLib library by calling
     * {@code Geodesic.WGS84.Inverse} with the latitude and longitude of the provided positions,
     * employing the {@code GeodesicMask.DISTANCE} mask. The resulting {@code GeodesicData} object's
     * {@code s12} field contains the computed distance in meters.
     *
     * @param p1 the first geographical position with latitude and longitude coordinates
     * @param p2 the second geographical position with latitude and longitude coordinates
     * @return the geodesic distance in meters between {@code p1} and {@code p2}
     */
    static double karneyDistance(Position p1, Position p2) {
        final GeodesicData geodesicData = Geodesic.WGS84.Inverse(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), GeodesicMask.DISTANCE);
        return geodesicData.s12;
    }

    /**
     * Calculates a precise geodesic distance between two {@link Point} instances.
     *
     * <p>This method delegates to {@link #karneyDistance(Position, Position)} by using the
     * underlying {@link Position} coordinates of the provided points. It is intended for
     * scenarios where high accuracy in distance calculation is required.</p>
     *
     * @param p1 the first point, providing latitude and longitude coordinates
     * @param p2 the second point, providing latitude and longitude coordinates
     * @return the precise distance between {@code p1} and {@code p2} in meters
     */
    public static double calculate(Point p1, Point p2) {
        return karneyDistance(p1.getCoordinates(), p2.getCoordinates());
    }

    /**
     * Calculates a precise distance between a Point and a LineString.
     *
     * <p>This method iterates through the segments and uses the GeographicLib Karney algorithm
     * to determine distances. It handles the geodesic nature of the segments by checking
     * azimuths to determine if the closest point lies within the segment or at an endpoint.
     * If within the segment, it calculates the cross-track distance on the ellipsoid.</p>
     *
     * @param p          the point
     * @param lineString the line string
     * @return the precise distance in meters
     */
    public static double calculate(Point p, LineString lineString) {
        double minDistance = Double.MAX_VALUE;
        List<Position> positions = lineString.getCoordinates();

        if (positions == null || positions.isEmpty()) {
            throw new IllegalArgumentException("LineString must contain at least one position.");
        }
        if (positions.size() == 1) {
            return karneyDistance(p.getCoordinates(), positions.getFirst());
        }

        for (int i = 0; i < positions.size() - 1; i++) {
            Position p1 = positions.get(i);
            Position p2 = positions.get(i + 1);

            GeodesicData gAP = Geodesic.WGS84.Inverse(p1.getLatitude(), p1.getLongitude(), p.getLatitude(), p.getLongitude(), GeodesicMask.DISTANCE | GeodesicMask.AZIMUTH);
            GeodesicData gAB = Geodesic.WGS84.Inverse(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), GeodesicMask.AZIMUTH);

            double azDiffA = Math.abs(gAP.azi1 - gAB.azi1);
            if (azDiffA > 180) azDiffA = 360 - azDiffA;

            double dist;
            if (azDiffA > 90) {
                // Closest is p1
                dist = gAP.s12;
            } else {
                GeodesicData gBP = Geodesic.WGS84.Inverse(p2.getLatitude(), p2.getLongitude(), p.getLatitude(), p.getLongitude(), GeodesicMask.DISTANCE | GeodesicMask.AZIMUTH);
                GeodesicData gBA = Geodesic.WGS84.Inverse(p2.getLatitude(), p2.getLongitude(), p1.getLatitude(), p1.getLongitude(), GeodesicMask.AZIMUTH);

                double azDiffB = Math.abs(gBP.azi1 - gBA.azi1);
                if (azDiffB > 180) azDiffB = 360 - azDiffB;

                if (azDiffB > 90) {
                    // Closest is p2
                    dist = gBP.s12;
                } else {
                    // Closest is on the segment. Use spherical cross-track approximation with ellipsoidal radius.
                    // WGS84 semi-major axis (equatorial radius) in meters
                    double R = WGS84_SEMI_MAJOR_AXIS;
                    dist = Math.abs(Math.asin(Math.sin(gAP.s12 / R) * Math.sin(Math.toRadians(azDiffA)))) * R;
                }
            }

            if (dist < minDistance) {
                minDistance = dist;
            }
        }
        return minDistance;
    }
}
