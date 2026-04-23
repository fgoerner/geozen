# GeoZen – Agent Instructions

GeoZen is a Kotlin library for working with geospatial data (GeoJSON, WKT/EWKT, coordinate reference systems, distance calculations). Published to Maven Central as `dev.goerner.geozen:geozen-core`.

## Build & Test

```sh
./gradlew build        # compile + test
./gradlew test         # tests only (Kotest + JUnit Platform)
```

- **Java 21+**, **Kotlin 2.3**, Gradle wrapper included.
- No code formatting plugin configured — follow existing style.

## Architecture

All source lives under `src/main/kotlin/dev/goerner/geozen/`:

| Package                  | Purpose                                                                                                                             |
|--------------------------|-------------------------------------------------------------------------------------------------------------------------------------|
| `model/`                 | Core domain: `Position`, `Geometry` (abstract), `CoordinateReferenceSystem`, `Feature`, `Reprojector`                               |
| `model/simple_geometry/` | `Point`, `LineString`, `Polygon`                                                                                                    |
| `model/multi_geometry/`  | `MultiPoint`, `MultiLineString`, `MultiPolygon`                                                                                     |
| `model/collections/`     | `GeometryCollection`, `FeatureCollection`                                                                                           |
| `jackson/`               | Jackson 3 (`tools.jackson`) module with per-type serializers & deserializers                                                        |
| `wkt/`                   | `WktSerializer` / `WktDeserializer` — stateless `object` singletons                                                                 |
| `calc/`                  | Distance calculators: `PreciseDistanceCalculator` (Karney), `ApproximateDistanceCalculator` (Haversine), plus geometry-pair helpers |

## Key Conventions

- **Coordinate order** is always `(longitude, latitude, altitude)` — matching GeoJSON, not lat/lon.
- `Position` is a `data class`; altitude defaults to `0.0`. All values must be finite.
- `Geometry` subclasses take an optional `CoordinateReferenceSystem` (default `WGS_84`). Only `WGS_84` (SRID 4326) and `WEB_MERCATOR` (SRID 3857) are supported.
- Serializers/deserializers are in matched pairs: e.g. `PointSerializer` + `PointDeserializer`, registered in `GeoZenModule`.
- WKT serializer/deserializer are singleton `object`s — not Jackson modules.
- Empty geometries throw `WktException` / are rejected — GeoJSON does not allow them.

## Testing

- Framework: **Kotest** (`FunSpec` style) with `kotest-assertions-core`.
- Tests mirror source packages under `src/test/kotlin/dev/goerner/geozen/`.
- Test structure follows a `//given` / `//when` / `//then` comment pattern.
- Use `shouldBe`, `shouldBeInstanceOf`, `shouldThrow<>` matchers.

## Dependencies

- `tools.jackson` (Jackson 3) — note the `tools.jackson` package, **not** `com.fasterxml.jackson`.
- `net.sf.geographiclib:GeographicLib-Java` — for precise geodesic calculations.
- See [README.md](README.md) for full usage examples.
