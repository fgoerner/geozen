package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPointTest {

    @Test
    public void testCoordinatesConstructor() {
        List<Position> coordinates = new ArrayList<>();
        coordinates.add(new Position(1.0, 2.0, 3.0));
        coordinates.add(new Position(4.0, 5.0, 6.0));

        MultiPoint multiPoint = new MultiPoint(coordinates);

        assertEquals(2, multiPoint.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WGS_84, multiPoint.getCoordinateReferenceSystem());
    }

    @Test
    public void testCoordinatesAndReferenceSystemConstructor() {
        List<Position> coordinates = new ArrayList<>();
        coordinates.add(new Position(1.0, 2.0, 3.0));
        coordinates.add(new Position(4.0, 5.0, 6.0));

        MultiPoint multiPoint = new MultiPoint(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(2, multiPoint.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiPoint.getCoordinateReferenceSystem());
    }
}
