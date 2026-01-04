package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.collections.FeatureCollection
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer

class FeatureCollectionDeserializer : ValueDeserializer<FeatureCollection>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): FeatureCollection {
        val rootNode = p.readValueAsTree<JsonNode>()

        val type = rootNode["type"].asString()
        require(
            type.equals(
                "FeatureCollection",
                ignoreCase = true
            )
        ) { "Invalid GeoJSON type: $type. Expected 'FeatureCollection'." }

        val featureDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Feature::class.java))

        val features = rootNode["features"].map { featureDeserializer.deserialize(it.traverse(ctxt), ctxt) as Feature }

        return FeatureCollection(features)
    }
}
