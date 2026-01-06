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

        val idNode = rootNode["id"]
        require(idNode != null && idNode.isString) { "Missing or invalid 'id' field in GeoJSON Feature." }
        val id = idNode.asString()

        val geometryNode = rootNode["geometry"]
        require(geometryNode != null && geometryNode.isObject) { "Missing or invalid 'geometry' field in GeoJSON Feature." }
        val geometryDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Geometry::class.java))
        val geometry = geometryDeserializer.deserialize(geometryNode.traverse(ctxt), ctxt) as Geometry

        val propertiesNode = rootNode["properties"]
        require(propertiesNode != null && propertiesNode.isObject) { "Missing or invalid 'properties' field in GeoJSON Feature." }
        val properties = propertiesNode.properties().associate { it.key to it.value.asString() }

        return Feature(id, geometry, properties)
    }
}
