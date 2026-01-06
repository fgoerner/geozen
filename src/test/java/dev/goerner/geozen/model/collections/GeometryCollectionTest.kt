package dev.goerner.geozen.model.collections;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeometryCollectionTest {

    @Test
    public void testGeometriesConstructor() {
        Geometry point = new Point(0.0, 0.0);
        Geometry lineString = new LineString(List.of());
        List<Geometry> geometries = new ArrayList<>();
        geometries.add(point);
        geometries.add(lineString);

        GeometryCollection geometryCollection = new GeometryCollection(geometries);

        assertEquals(2, geometryCollection.getGeometries().size());
    }
}
