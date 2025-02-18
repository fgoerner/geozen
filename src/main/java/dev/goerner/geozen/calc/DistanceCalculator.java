package dev.goerner.geozen.calc;

import dev.goerner.geozen.model.Point;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicMask;

public class DistanceCalculator {

	public static double karneyDistance(Point p1, Point p2) {
		final GeodesicData geodesicData = Geodesic.WGS84.Inverse(p1.getLatitude(), p1.getLongitude(), p2.getLatitude(), p2.getLongitude(), GeodesicMask.DISTANCE);
		return geodesicData.s12;
	}

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
