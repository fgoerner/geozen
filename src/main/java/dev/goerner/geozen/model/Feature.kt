package dev.goerner.geozen.model

/**
 * A [Feature] represents a single spatial object in space. It is defined by an optional [.id], an optional
 * [.geometry] and an optional map of [.properties].
 */
class Feature(
    val geometry: Geometry,
    val id: String? = null,
    val properties: Map<String, String> = emptyMap()
)
