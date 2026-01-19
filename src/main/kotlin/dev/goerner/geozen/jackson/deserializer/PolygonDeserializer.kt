package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class PolygonDeserializer : AbstractGeometryDeserializer<Polygon>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Polygon {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "Polygon")

        val coordinatesNode = rootNode["coordinates"]
        require(coordinatesNode != null && coordinatesNode.isArray) { "Invalid or missing 'coordinates' field for Polygon geometry." }
        val coordinates = coordinatesNode.map { ringNode ->
            require(ringNode != null && ringNode.isArray) { "Invalid linear ring in 'coordinates' field for Polygon geometry." }
            ringNode.map { parsePosition(it) }
        }

        return Polygon(coordinates)
    }
}
