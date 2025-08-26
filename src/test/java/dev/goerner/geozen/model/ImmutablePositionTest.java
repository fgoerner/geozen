package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class ImmutablePositionTest {

    @Test
    public void testRecordConstructor() {
        ImmutablePosition position = new ImmutablePosition(1.0, 2.0, 3.0);

        assertEquals(1.0, position.longitude());
        assertEquals(2.0, position.latitude());
        assertEquals(3.0, position.altitude());
    }

    @Test
    public void testEmptyConstructor() {
        ImmutablePosition position = new ImmutablePosition();

        assertEquals(0.0, position.longitude());
        assertEquals(0.0, position.latitude());
        assertEquals(0.0, position.altitude());
    }

    @Test
    public void testTwoParameterConstructor() {
        ImmutablePosition position = new ImmutablePosition(1.0, 2.0);

        assertEquals(1.0, position.longitude());
        assertEquals(2.0, position.latitude());
        assertEquals(0.0, position.altitude());
    }

    @Test
    public void testFromPosition() {
        Position mutablePosition = new Position(1.0, 2.0, 3.0);
        ImmutablePosition immutablePosition = ImmutablePosition.from(mutablePosition);

        assertEquals(mutablePosition.getLongitude(), immutablePosition.longitude());
        assertEquals(mutablePosition.getLatitude(), immutablePosition.latitude());
        assertEquals(mutablePosition.getAltitude(), immutablePosition.altitude());
    }

    @Test
    public void testToPosition() {
        ImmutablePosition immutablePosition = new ImmutablePosition(1.0, 2.0, 3.0);
        Position mutablePosition = immutablePosition.toPosition();

        assertEquals(immutablePosition.longitude(), mutablePosition.getLongitude());
        assertEquals(immutablePosition.latitude(), mutablePosition.getLatitude());
        assertEquals(immutablePosition.altitude(), mutablePosition.getAltitude());
    }

    @Test
    public void testRecordEquality() {
        ImmutablePosition position1 = new ImmutablePosition(1.0, 2.0, 3.0);
        ImmutablePosition position2 = new ImmutablePosition(1.0, 2.0, 3.0);
        ImmutablePosition position3 = new ImmutablePosition(1.0, 2.0, 4.0);

        assertEquals(position1, position2);
        assertEquals(position1.hashCode(), position2.hashCode());
        assertNotSame(position1, position3);
    }

    @Test
    public void testRecordToString() {
        ImmutablePosition position = new ImmutablePosition(1.0, 2.0, 3.0);
        String expectedString = "ImmutablePosition[longitude=1.0, latitude=2.0, altitude=3.0]";
        
        assertEquals(expectedString, position.toString());
    }
}