# GeoZen

[![Maven Central](https://img.shields.io/maven-central/v/dev.goerner.geozen/geozen-core)](https://central.sonatype.com/artifact/dev.goerner.geozen/geozen-core)
[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
![CodeRabbit Pull Request Reviews](https://img.shields.io/coderabbit/prs/github/fgoerner/geozen?utm_source=oss&utm_medium=github&utm_campaign=fgoerner%2Fgeozen&labelColor=171717&color=FF570A&link=https%3A%2F%2Fcoderabbit.ai&label=CodeRabbit+Reviews)

GeoZen is a Java library for working with geodata, providing a robust data model compatible with GeoJSON and WKT/EWKT,
seamless integration with Jackson for serialization and deserialization, support for coordinate reference systems,
and utilities for geometric calculations.

## Features

- **GeoJSON Support**: Complete data model with Jackson integration for serialization/deserialization
- **WKT/EWKT Support**: Serialization and deserialization of Well-Known Text and Extended Well-Known Text formats
- **Coordinate Reference Systems**: Support for WGS 84 and Web Mercator coordinate systems with SRID handling
- **Distance Calculations**: Geodesic distance calculations using both Karney's algorithm and Haversine formula
- **Comprehensive Geometry Types**: Point, LineString, Polygon, MultiPoint, MultiLineString, MultiPolygon, and GeometryCollection
- **Feature Support**: Full support for GeoJSON Features and FeatureCollections

## Requirements

- Java 21 or higher

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

```java
ObjectMapper om = new ObjectMapper();

// Register the GeoZen module to enable GeoJSON support
om.registerModule(new GeoZenModule());
```

### Deserializing a GeoJSON Point

```java
String geoJsonString = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}";

// Deserialize to a specific geometry type
Point point = om.readValue(geoJsonString, Point.class);

// Or to a generic Geometry if the geometry type is unknown
Geometry geometry = om.readValue(geoJsonString, Geometry.class);
```

### Serializing a LineString

```java
ArrayList<Position> coordinates = new ArrayList<>();
coordinates.add(new Position(1.0, 2.0, 3.0));
coordinates.add(new Position(4.0, 5.0, 6.0));
LineString lineString = new LineString(coordinates);

String geoJsonString = om.writeValueAsString(lineString);
// Result: {"type":"LineString","coordinates":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}
```

### Working with WKT/EWKT

```java
// Deserialize WKT
Point point = WktDeserializer.deserialize("POINT (30 10)");

// Deserialize EWKT with SRID
Point pointWithCrs = WktDeserializer.deserialize("SRID=4326;POINT (30 10)");

// Serialize to WKT
String wkt = WktSerializer.serialize(point);
// Result: "POINT (30 10)"

// Serialize to EWKT with SRID
String ewkt = WktSerializer.serializeWithSrid(point);
// Result: "SRID=4326;POINT (30 10)"
```

### Calculating Distances

```java
Point p1 = new Point(13.4050, 52.5200); // Berlin
Point p2 = new Point(2.3522, 48.8566);  // Paris

// Using Karney's algorithm (more accurate)
double distance = DistanceCalculator.karneyDistance(p1, p2);

// Using Haversine formula (faster, slightly less accurate)
double distance2 = DistanceCalculator.haversineDistance(p1, p2);
```

### Working with Coordinate Reference Systems

```java
// Create a point with a specific CRS
Point point = new Point(13.4050, 52.5200, CoordinateReferenceSystem.WGS_84);

// Get the CRS
CoordinateReferenceSystem crs = point.getCrs();
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
