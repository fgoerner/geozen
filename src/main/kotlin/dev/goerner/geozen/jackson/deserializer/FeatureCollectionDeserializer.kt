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

        val typeNode = rootNode["type"]
        require(typeNode != null && typeNode.isString) { "Missing or invalid 'type' field in GeoJSON." }
        val type = typeNode.asString()
        require(type == "FeatureCollection") { "Invalid GeoJSON type: $type. Expected 'FeatureCollection'." }

        val featureDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Feature::class.java))

        val featuresNode = rootNode["features"]
        require(featuresNode != null && featuresNode.isArray) { "Missing or invalid 'features' field in FeatureCollection." }
        val features = featuresNode.map { featureDeserializer.deserialize(it.traverse(ctxt), ctxt) as Feature }

        return FeatureCollection(features)
    }
}
