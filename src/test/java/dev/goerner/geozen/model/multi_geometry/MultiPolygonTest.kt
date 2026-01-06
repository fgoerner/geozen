package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPolygonTest {

    @Test
    public void testCoordinatesConstructor() {
        List<List<List<Position>>> coordinates = new ArrayList<>();
        List<List<Position>> polygon = new ArrayList<>();
        List<Position> exteriorRing = new ArrayList<>();
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        exteriorRing.add(new Position(4.0, 5.0, 6.0));
        exteriorRing.add(new Position(7.0, 8.0, 9.0));
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        polygon.add(exteriorRing);
        List<Position> interiorRing = new ArrayList<>();
        interiorRing.add(new Position(10.0, 11.0, 12.0));
        interiorRing.add(new Position(13.0, 14.0, 15.0));
        interiorRing.add(new Position(16.0, 17.0, 18.0));
        interiorRing.add(new Position(10.0, 11.0, 12.0));
        polygon.add(interiorRing);
        coordinates.add(polygon);

        MultiPolygon multiPolygon = new MultiPolygon(coordinates);

        assertEquals(1, multiPolygon.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WGS_84, multiPolygon.getCoordinateReferenceSystem());
    }

    @Test
    public void testCoordinatesAndReferenceSystemConstructor() {
        List<List<List<Position>>> coordinates = new ArrayList<>();
        List<List<Position>> polygon = new ArrayList<>();
        List<Position> exteriorRing = new ArrayList<>();
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        exteriorRing.add(new Position(4.0, 5.0, 6.0));
        exteriorRing.add(new Position(7.0, 8.0, 9.0));
        exteriorRing.add(new Position(1.0, 2.0, 3.0));
        polygon.add(exteriorRing);
        List<Position> interiorRing = new ArrayList<>();
        interiorRing.add(new Position(10.0, 11.0, 12.0));
        interiorRing.add(new Position(13.0, 14.0, 15.0));
        interiorRing.add(new Position(16.0, 17.0, 18.0));
        interiorRing.add(new Position(10.0, 11.0, 12.0));
        polygon.add(interiorRing);
        coordinates.add(polygon);

        MultiPolygon multiPolygon = new MultiPolygon(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(1, multiPolygon.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPolygon.getCoordinateReferenceSystem());
    }
}
