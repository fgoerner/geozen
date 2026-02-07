# GeoZen

[![Maven Central](https://img.shields.io/maven-central/v/dev.goerner.geozen/geozen-core)](https://central.sonatype.com/artifact/dev.goerner.geozen/geozen-core)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-7F52FF)](https://kotlinlang.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/fgoerner/geozen?utm_source=oss&utm_medium=github&utm_campaign=fgoerner%2Fgeozen&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

GeoZen is a Kotlin library for working with geospatial data, providing a robust data model compatible with GeoJSON and WKT/EWKT,
seamless integration with Jackson 3 for serialization and deserialization, support for coordinate reference systems,
and utilities for geometric calculations.

## Features

- **GeoJSON Support**: Complete data model with Jackson 3 integration for serialization/deserialization
- **WKT/EWKT Support**: Serialization and deserialization of Well-Known Text and Extended Well-Known Text formats
- **Coordinate Reference Systems**: Support for WGS 84 and Web Mercator coordinate systems with SRID handling
- **Distance Calculations**: Geodesic distance calculations using both Karney's algorithm (precise) and Haversine formula (fast approximation)
- **Comprehensive Geometry Types**: Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, and GeometryCollection
- **Feature Support**: Full support for GeoJSON Features and FeatureCollections
- **Kotlin & Java Compatibility**: Written in Kotlin with full Java interoperability

## Requirements

- Java 21 or higher
- Kotlin 2.3.0 or higher (if using Kotlin)

## Installation

GeoZen is available on Maven Central. Add the following dependency to your project:

### Maven

```xml
<dependency>
   <groupId>dev.goerner.geozen</groupId>
   <artifactId>geozen-core</artifactId>
   <version>0.6.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'dev.goerner.geozen:geozen-core:0.6.0'
```

## Usage

### Preparing the ObjectMapper

```kotlin
val objectMapper = JsonMapper.builder()
    .addModule(GeoZenModule())
    .build()
```

### Deserializing a GeoJSON Point

```kotlin
val geoJsonString = """{"type":"Point","coordinates":[1.0,2.0,3.0]}"""

// Deserialize to a specific geometry type
val point = objectMapper.readValue(geoJsonString, Point::class.java)

// Or to a generic Geometry if the geometry type is unknown
val geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)
```

### Serializing a LineString

```kotlin
val coordinates = listOf(
    Position(1.0, 2.0, 3.0),
    Position(4.0, 5.0, 6.0)
)
val lineString = LineString(coordinates)

val geoJsonString = objectMapper.writeValueAsString(lineString)
// Result: {"type":"LineString","coordinates":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}
```

### Working with WKT/EWKT

```kotlin
val wktSerializer = WktSerializer()
val wktDeserializer = WktDeserializer()

// Deserialize WKT
val point: Point = wktDeserializer.deserialize("POINT (30 10)") as Point

// Deserialize EWKT with SRID
val pointWithCrs: Point = wktDeserializer.deserialize("SRID=4326;POINT (30 10)") as Point

// Serialize to WKT
val wkt = wktSerializer.serialize(point)
// Result: "POINT (30 10)"

// Serialize to EWKT with SRID
val ewkt = wktSerializer.serializeWithSrid(point)
// Result: "SRID=4326;POINT (30 10)"
```

### Calculating Distances

GeoZen provides multiple ways to calculate distances between geometries:

#### Supported Distance Calculations

The following matrix shows which geometry type combinations support distance calculations:

| From ↓ / To →          | Point | LineString | Polygon | MultiPoint | MultiLineString | MultiPolygon | GeometryCollection |
|------------------------|-------|------------|---------|------------|-----------------|--------------|--------------------|
| **Point**              | ✅     | ✅          | ✅       | ❌          | ❌               | ❌            | ❌                  |
| **LineString**         | ✅     | 🟡         | ❌       | ❌          | ❌               | ❌            | ❌                  |
| **Polygon**            | ✅     | ❌          | ❌       | ❌          | ❌               | ❌            | ❌                  |
| **MultiPoint**         | ❌     | ❌          | ❌       | ❌          | ❌               | ❌            | ❌                  |
| **MultiLineString**    | ❌     | ❌          | ❌       | ❌          | ❌               | ❌            | ❌                  |
| **MultiPolygon**       | ❌     | ❌          | ❌       | ❌          | ❌               | ❌            | ❌                  |
| **GeometryCollection** | ❌     | ❌          | ❌       | ❌          | ❌               | ❌            | ❌                  |

**Legend:**
- ✅ Supported for both precise (Karney's algorithm) and approximate (Haversine formula) distance calculations
- 🟡 Only approximate (Haversine formula) calculation supported - precise calculation not yet implemented
- ❌ Not yet supported

**Note:** Distance calculations are commutative - if `A.distanceTo(B)` is supported, then `B.distanceTo(A)` is also supported and will return the same result.

#### Using Distance Calculators

```kotlin
val berlin = Point(13.4050, 52.5200) // Berlin
val paris = Point(2.3522, 48.8566)   // Paris

// Using Karney's algorithm (more accurate, slower)
val distance1 = PreciseDistanceCalculator.calculate(berlin, paris)

// Using Haversine formula (faster, slightly less accurate)
val distance2 = ApproximateDistanceCalculator.calculate(berlin, paris)
```

#### Using Geometry Methods

```kotlin
val berlin = Point(13.4050, 52.5200)
val paris = Point(2.3522, 48.8566)

// Using the exact distance method (Karney's algorithm)
val exactDistance = berlin.getExactDistanceTo(paris)

// Using the fast approximation method (Haversine formula)
val fastDistance = berlin.getFastDistanceTo(paris)
```

### Working with Coordinate Reference Systems

```kotlin
// Create a point with a specific CRS
val point = Point(13.4050, 52.5200, coordinateReferenceSystem = CoordinateReferenceSystem.WGS_84)

// Get the CRS
val crs = point.coordinateReferenceSystem
```

Note: Coordinates in GeoJSON follow the format `[longitude, latitude, altitude]`, where altitude is optional. If no
altitude is provided, it defaults to 0.

## Current Status

GeoZen provides a comprehensive set of features for working with geospatial data:

- ✅ Core data model implementation
- ✅ GeoJSON serialization and deserialization via Jackson
- ✅ WKT/EWKT serialization and deserialization
- ✅ Coordinate reference system support (WGS 84, Web Mercator)
- ✅ Distance calculations (Karney's algorithm and Haversine formula)
- 🚧 CRS conversion capabilities (planned for future releases)
- 🚧 Additional geometric calculations (planned for future releases)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

GeoZen is developed by Felix Görner
