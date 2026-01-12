package dev.goerner.geozen.model

/**
 * A [CoordinateReferenceSystem] defines how coordinates are mapped to positions in space. It enables consistent
 * and accurate geodetic calculations.
 */
enum class CoordinateReferenceSystem {
    WGS_84,
    WEB_MERCATOR
}
