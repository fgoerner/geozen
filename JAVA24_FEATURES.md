# Java 24 Features in GeoZen

This document outlines the Java 24 features implemented in the GeoZen library.

## Features Implemented

### 1. Sealed Classes

The `Geometry` abstract class has been made sealed to restrict inheritance to known geometry types:

```java
public abstract sealed class Geometry
    permits Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, GeometryCollection {
```

**Benefits:**
- Provides better type safety at compile time
- Enables exhaustive pattern matching in switch expressions
- Makes the API more predictable by restricting which classes can extend Geometry

### 2. Records

Added `ImmutablePosition` as a modern alternative to the mutable `Position` class:

```java
public record ImmutablePosition(double longitude, double latitude, double altitude) {
    // Constructors and utility methods
}
```

**Benefits:**
- Immutable data structure
- Automatic generation of equals(), hashCode(), toString()
- Reduced boilerplate code
- Better thread safety

### 3. Enhanced Pattern Matching

Updated serializers and deserializers to leverage exhaustive pattern matching with sealed classes:

```java
return switch (geometry) {
    case Point point -> "Point at (" + point.getLongitude() + ", " + point.getLatitude() + ")";
    case LineString lineString -> "LineString with " + lineString.getCoordinates().size() + " points";
    // ... other cases
    // No default case needed - exhaustive matching with sealed class
};
```

**Benefits:**
- Compile-time guarantee that all cases are handled
- No need for default cases with sealed classes
- Better type safety and fewer runtime errors

## Usage Examples

### Using ImmutablePosition

```java
// Create immutable position
ImmutablePosition pos = new ImmutablePosition(1.0, 2.0, 3.0);

// Convert between mutable and immutable
Position mutable = new Position(1.0, 2.0);
ImmutablePosition immutable = ImmutablePosition.from(mutable);
Position backToMutable = immutable.toPosition();
```

### Using GeometryUtils with Pattern Matching

```java
// Get descriptive type name
String description = GeometryUtils.getGeometryTypeName(myGeometry);

// Count coordinate points
int pointCount = GeometryUtils.countCoordinatePoints(myGeometry);
```

## Migration Notes

- The sealed `Geometry` class is backward compatible
- `ImmutablePosition` is an addition, not a replacement for `Position`
- Enhanced pattern matching in internal serializers improves type safety
- All changes maintain API compatibility

## Future Considerations

- Consider migrating more data classes to records
- Explore additional pattern matching opportunities
- Evaluate string templates for error messages and logging