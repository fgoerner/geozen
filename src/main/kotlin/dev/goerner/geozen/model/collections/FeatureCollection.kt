package dev.goerner.geozen.model.collections

import dev.goerner.geozen.model.Feature

/**
 * A [FeatureCollection] is a collection of [Features][Feature].
 */
data class FeatureCollection(val features: List<Feature>)
