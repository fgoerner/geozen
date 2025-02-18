package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Point;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicMask;

public class DistanceCalculator {

	/**
	 * Calculates the distance between two geographical points using the Karney algorithm.
	 *
	 * <p>This method utilizes the GeographicLib library by calling
	 * {@code Geodesic.WGS84.Inverse} with the latitude and longitude of the provided points,
	 * employing the {@code GeodesicMask.DISTANCE} mask. The resulting {@code GeodesicData} object's
	 * {@code s12} field contains the computed distance in meters.
	 *
	 * @param p1 the first geographical point with latitude and longitude coordinates
	 * @param p2 the second geographical point with latitude and longitude coordinates
	 * @return the geodesic distance in meters between {@code p1} and {@code p2}
	 */
	public static double karneyDistance(Point p1, Point p2) {
		final GeodesicData geodesicData = Geodesic.WGS84.Inverse(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), GeodesicMask.DISTANCE);
		return geodesicData.s12;
	}

	/**
	 * Calculates the distance between two geographical points using the Haversine formula.
	 *
	 * <p>
	 * This method converts the latitude and longitude of the provided {@code Point} objects from
	 * degrees to radians, computes the differences in latitude and longitude, and calculates the
	 * distance based on the Haversine formula. The Earth's radius is assumed to be 6,371,008.8 meters.
	 * </p>
	 *
	 * @param p1 the first geographical point, with latitude and longitude in degrees
	 * @param p2 the second geographical point, with latitude and longitude in degrees
	 * @return the computed distance between {@code p1} and {@code p2} in meters
	 */
	public static double haversineDistance(Point p1, Point p2) {
		final double lat1 = Math.toRadians(p1.getLatitude());
		final double lat2 = Math.toRadians(p2.getLatitude());

		final double lon1 = Math.toRadians(p1.getLongitude());
		final double lon2 = Math.toRadians(p2.getLongitude());

		final double deltaLat = lat2 - lat1;
		final double deltaLon = lon2 - lon1;

		final double distanceFactor = 1.0 - Math.cos(deltaLat) + Math.cos(lat1) * Math.cos(lat2) * (1.0 - Math.cos(deltaLon));

		return 2.0 * 6_371_008.8 * Math.asin(Math.sqrt(distanceFactor / 2.0));
	}
}
