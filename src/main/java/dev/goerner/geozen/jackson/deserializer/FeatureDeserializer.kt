package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.Geometry
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer

class FeatureDeserializer : ValueDeserializer<Feature>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Feature {
        val rootNode = p.readValueAsTree<JsonNode>()

        val type = rootNode["type"].asString()
        require(type.equals("Feature", ignoreCase = true)) { "Invalid GeoJSON type: $type. Expected 'Feature'." }

        val id = rootNode["id"].asString()

        val geometryDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Geometry::class.java))
        val geometry = geometryDeserializer.deserialize(rootNode.get("geometry").traverse(ctxt), ctxt) as Geometry

        val properties = rootNode["properties"].properties().associate { it.key to it.value.asString() }

        return Feature(id, geometry, properties)
    }
}
