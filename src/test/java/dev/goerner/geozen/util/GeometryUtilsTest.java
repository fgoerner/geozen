package dev.goerner.geozen.util;

import dev.goerner.geozen.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeometryUtilsTest {

    @Test
    public void testGetGeometryTypeNameForPoint() {
        Point point = new Point(1.0, 2.0);
        String result = GeometryUtils.getGeometryTypeName(point);
        
        assertEquals("Point at (1.0, 2.0)", result);
    }

    @Test
    public void testGetGeometryTypeNameForLineString() {
        LineString lineString = new LineString();
        lineString.addCoordinate(new Position(1.0, 2.0));
        lineString.addCoordinate(new Position(3.0, 4.0));
        
        String result = GeometryUtils.getGeometryTypeName(lineString);
        
        assertEquals("LineString with 2 points", result);
    }

    @Test
    public void testGetGeometryTypeNameForGeometryCollection() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        geometries.add(new Point(1.0, 2.0));
        geometries.add(new Point(3.0, 4.0));
        
        GeometryCollection collection = new GeometryCollection(geometries);
        String result = GeometryUtils.getGeometryTypeName(collection);
        
        assertEquals("GeometryCollection with 2 geometries", result);
    }

    @Test
    public void testCountCoordinatePointsForPoint() {
        Point point = new Point(1.0, 2.0);
        int count = GeometryUtils.countCoordinatePoints(point);
        
        assertEquals(1, count);
    }

    @Test
    public void testCountCoordinatePointsForLineString() {
        LineString lineString = new LineString();
        lineString.addCoordinate(new Position(1.0, 2.0));
        lineString.addCoordinate(new Position(3.0, 4.0));
        lineString.addCoordinate(new Position(5.0, 6.0));
        
        int count = GeometryUtils.countCoordinatePoints(lineString);
        
        assertEquals(3, count);
    }

    @Test
    public void testCountCoordinatePointsForGeometryCollection() {
        ArrayList<Geometry> geometries = new ArrayList<>();
        
        // Add a point (1 coordinate)
        geometries.add(new Point(1.0, 2.0));
        
        // Add a line string (2 coordinates)
        LineString lineString = new LineString();
        lineString.addCoordinate(new Position(3.0, 4.0));
        lineString.addCoordinate(new Position(5.0, 6.0));
        geometries.add(lineString);
        
        GeometryCollection collection = new GeometryCollection(geometries);
        int count = GeometryUtils.countCoordinatePoints(collection);
        
        assertEquals(3, count); // 1 from point + 2 from line string
    }

    @Test
    public void testExhaustivePatternMatching() {
        // Test that all geometry types are handled without exceptions
        Geometry[] geometries = {
            new Point(0, 0),
            new LineString(),
            new Polygon(),
            new MultiPoint(),
            new MultiLineString(), 
            new MultiPolygon(),
            new GeometryCollection()
        };
        
        for (Geometry geometry : geometries) {
            // Should not throw any exceptions due to exhaustive matching
            String typeName = GeometryUtils.getGeometryTypeName(geometry);
            assertTrue(typeName.length() > 0);
            
            int count = GeometryUtils.countCoordinatePoints(geometry);
            assertTrue(count >= 0);
        }
    }
}