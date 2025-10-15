package dev.goerner.geozen.model.multi_geometry;

import dev.goerner.geozen.model.CoordinateReferenceSystem;
import dev.goerner.geozen.model.Position;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiLineStringTest {

    @Test
    public void testCoordinatesConstructor() {
        List<List<Position>> coordinates = new ArrayList<>();
        List<Position> lineString = new ArrayList<>();
        lineString.add(new Position(1.0, 2.0, 3.0));
        lineString.add(new Position(4.0, 5.0, 6.0));
        coordinates.add(lineString);

        MultiLineString multiLineString = new MultiLineString(coordinates);

        assertEquals(1, multiLineString.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WGS_84, multiLineString.getCoordinateReferenceSystem());
    }

    @Test
    public void testCoordinatesAndReferenceSystemConstructor() {
        List<List<Position>> coordinates = new ArrayList<>();
        List<Position> lineString = new ArrayList<>();
        lineString.add(new Position(1.0, 2.0, 3.0));
        lineString.add(new Position(4.0, 5.0, 6.0));
        coordinates.add(lineString);

        MultiLineString multiLineString = new MultiLineString(coordinates, CoordinateReferenceSystem.WEB_MERCATOR);

        assertEquals(1, multiLineString.getCoordinates().size());
        assertEquals(CoordinateReferenceSystem.WEB_MERCATOR, multiLineString.getCoordinateReferenceSystem());
    }
}
