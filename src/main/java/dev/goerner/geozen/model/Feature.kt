package dev.goerner.geozen.model

/**
 * A [Feature] represents a single spatial object in space. It is defined by an optional [.id], an optional
 * [.geometry] and an optional map of [.properties].
 */
class Feature(
    val id: String? = null,
    val geometry: Geometry? = null,
    val properties: Map<String, String>? = emptyMap()
)
