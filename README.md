# GeoZen

[![Maven Central](https://img.shields.io/maven-central/v/dev.goerner.geozen/geozen-core)](https://central.sonatype.com/artifact/dev.goerner.geozen/geozen-core)
[![Java](https://img.shields.io/badge/Java-23%2B-blue)](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

GeoZen is a Java library for working with geodata, providing a robust data model compatible with GeoJSON and seamless
integration with Jackson for serialization and deserialization. The library supports various coordinate reference
systems and includes utilities for geometric calculations.

## Features

- GeoJSON-compatible data model
- Jackson integration for GeoJSON serialization/deserialization
- Support for different coordinate reference systems (e.g. WGS 84, Web Mercator) [Coming Soon]
- CRS conversion capabilities [Coming Soon]
- Simple geometric calculations (e.g., distance calculations) [Coming Soon]

## Requirements

- Java 23 or higher

## Installation

GeoZen is available on Maven Central. Add the following dependency to your project:

### Maven

```xml

<dependency>
	<groupId>dev.goerner.geozen</groupId>
	<artifactId>geozen-core</artifactId>
	<version>0.2.0</version>
</dependency>
```

### Gradle

```groovy
implementation 'dev.goerner.geozen:geozen-core:0.2.0'
```

## Usage

### Deserializing a GeoJSON Point

```java
void deserializePoint() {
	ObjectMapper om = new ObjectMapper();
	// Register the GeoZen module to enable GeoJSON support
	om.registerModule(new GeoZenModule());

	// Parse a GeoJSON Point
	String geoJsonString = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}";

	// Deserialize to a specific geometry type
	Point point = om.readValue(geoJsonString, Point.class);

	// Or if the geometry type is unknown
	Geometry geometry = om.readValue(geoJsonString, Geometry.class);
}
```

### Serializing a LineString

```java
void serializeLineString() {
	ArrayList<Position> coordinates = new ArrayList<>();
	coordinates.add(new Position(1.0, 2.0, 3.0));
	coordinates.add(new Position(4.0, 5.0, 6.0));
	LineString lineString = new LineString(coordinates);

	String geoJsonString = om.writeValueAsString(lineString);
// Result: {"type":"LineString","coordinates":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}
}
```

Note: Coordinates in GeoJSON follow the format `[longitude, latitude, altitude]`, where altitude is optional. If no
altitude is provided, it defaults to 0.

## Current Status

GeoZen is under active development. The current version (0.2.0) focuses on:

- Core data model implementation
- GeoJSON serialization and deserialization via Jackson

Additional features, including coordinate reference system conversions and geometric calculations, are planned for
future releases.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Author

GeoZen is developed by Felix Görner
