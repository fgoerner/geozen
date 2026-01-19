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

        val typeNode = rootNode["type"]
        require(typeNode != null && typeNode.isString) { "Missing or invalid 'type' field in GeoJSON Feature." }
        val type = typeNode.asString()
        require(type == "Feature") { "Invalid GeoJSON type: $type. Expected 'Feature'." }

        val id = rootNode["id"]?.asString()

        val geometryNode = rootNode["geometry"]
        val geometry = if (geometryNode != null && !geometryNode.isNull) {
            ctxt.readValue(geometryNode.traverse(ctxt), Geometry::class.java)
        } else {
            null
        }

        val propertiesNode = rootNode["properties"]
        val properties = if (propertiesNode != null && propertiesNode.isObject) {
            propertiesNode.properties().associate { it.key to nodeToValue(it.value) }
        } else {
            emptyMap()
        }

        return Feature(id, geometry, properties)
    }

    private fun nodeToValue(node: JsonNode): Any? = when {
        node.isNull -> null
        node.isBoolean -> node.asBoolean()
        node.isNumber -> node.numberValue()
        node.isString -> node.asString()
        else -> node.toString() // fallback for objects/arrays
    }
}

