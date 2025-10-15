package dev.goerner.geozen.model.simple_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PolygonTest {

    @Test
    public void testExteriorRingConstructor() {
        List<Position> exteriorRing = new ArrayList<>();
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        exteriorRing.add(new Position(4.0, 5.0, 6.0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(exteriorRing);
        Polygon polygon = new Polygon(coordinates);

        assertEquals(1, polygon.getCoordinates().size());
        assertEquals(exteriorRing, polygon.getCoordinates().getFirst());
        assertEquals(CoordinateReferenceSystem.WGS_84, polygon.getCoordinateReferenceSystem());
    }

    @Test
    public void testExteriorRingAndReferenceSystemConstructor() {
        List<Position> exteriorRing = new ArrayList<>();
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        exteriorRing.add(new Position(4.0, 5.0, 6.0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(exteriorRing);
        Polygon polygon = new Polygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(1, polygon.getCoordinates().size());
        assertEquals(exteriorRing, polygon.getCoordinates().getFirst());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, polygon.getCoordinateReferenceSystem());
    }

    @Test
    public void testGetExteriorRing() {
        List<Position> exteriorRing = new ArrayList<>();
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        exteriorRing.add(new Position(4.0, 5.0, 6.0));
        List<List<Position>> coordinates = new ArrayList<>();
        coordinates.add(exteriorRing);
        Polygon polygon = new Polygon(coordinates);

        assertEquals(exteriorRing, polygon.getExteriorRing());
    }
}
