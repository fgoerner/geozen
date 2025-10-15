package dev.goerner.geozen.model.simple_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointTest {

    @Test
    public void testLongitudeAndLatitudeConstructor() {
        Point point = new Point(1.0, 2.0);

        assertEquals(1.0, point.getLongitude());
        assertEquals(2.0, point.getLatitude());
        assertEquals(0, point.getAltitude());
        assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
    }

    @Test
    public void testLongitudeAndLatitudeAndAltitudeConstructor() {
        Point point = new Point(1.0, 2.0, 3.0);

        assertEquals(1.0, point.getLongitude());
        assertEquals(2.0, point.getLatitude());
        assertEquals(3.0, point.getAltitude());
        assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
    }

    @Test
    public void testCoordinatesConstructor() {
        Position coordinates = new Position(1.0, 2.0, 3.0);
        Point point = new Point(coordinates);

        assertEquals(coordinates, point.getCoordinates());
        assertEquals(CoordinateReferenceSystem.WGS_84, point.getCoordinateReferenceSystem());
    }

    @Test
    public void testLongitudeAndLatitudeAndReferenceSystemConstructor() {
        Point point = new Point(1.0, 2.0, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(1.0, point.getLongitude());
        assertEquals(2.0, point.getLatitude());
        assertEquals(0, point.getAltitude());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
    }

    @Test
    public void testLongitudeAndLatitudeAndAltitudeAndReferenceSystemConstructor() {
        Point point = new Point(1.0, 2.0, 3.0, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(1.0, point.getLongitude());
        assertEquals(2.0, point.getLatitude());
        assertEquals(3.0, point.getAltitude());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
    }

    @Test
    public void testCoordinatesAndReferenceSystemConstructor() {
        Position coordinates = new Position(1.0, 2.0, 3.0);
        Point point = new Point(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(coordinates, point.getCoordinates());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, point.getCoordinateReferenceSystem());
    }
}
