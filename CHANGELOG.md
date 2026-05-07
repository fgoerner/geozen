# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0.1] - 2026-05-07

### Changed

- Added `@JvmOverloads` constructor annotations to all model classes (`Position`, `Point`, `LineString`, `Polygon`, `MultiPoint`, `MultiLineString`, `MultiPolygon`, `GeometryCollection`, `Feature`) for improved Java interoperability

## [1.0.0] - 2026-04-24

### Added

- `DistanceCalculator` interface as a common abstraction for `PreciseDistanceCalculator` and `ApproximateDistanceCalculator`
- Distance calculations between `LineString` and `MultiPoint`, `MultiLineString`, `MultiPolygon`
- Distance calculations between `Polygon` and `MultiPoint`, `MultiLineString`, `MultiPolygon`
- Distance calculations between `MultiPoint` and `MultiPoint`, `MultiLineString`, `MultiPolygon`
- Distance calculations between `MultiLineString` and `MultiLineString`, `MultiPolygon`
- Distance calculations between `MultiPolygon` and `MultiPolygon`
- Distance calculations involving `GeometryCollection`
- CRS reprojection via `Geometry.reprojectTo(CoordinateReferenceSystem)` (supports WGS 84 ↔ Web Mercator)

### Fixed

- Distance calculations for `MultiLineString`, `MultiPolygon`, and `MultiPoint` now correctly honor the geometry's coordinate reference system
- `MultiPoint.reprojectTo()` now returns a `MultiPoint` instead of a `LineString`

### Changed

- Refactored distance calculation methods and intersection detection logic across geometry types
- Refactored Jackson deserializers to use `Iterable` for JSON node mapping
- Updated all dependencies to latest versions (Kotlin, Jackson, Kotest, GitHub Actions)
- Removed custom CodeQL workflow and configuration

[Unreleased]: https://github.com/fgoerner/geozen/compare/v1.0.1...HEAD
[1.0.1]: https://github.com/fgoerner/geozen/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/fgoerner/geozen/compare/v0.6.0...v1.0.0
