package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.simple_geometry.LineString
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class LineStringDeserializer : AbstractGeometryDeserializer<LineString>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LineString {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "LineString")

        val coordinatesNode = rootNode["coordinates"]
        require(coordinatesNode != null && coordinatesNode.isArray) { "Invalid or missing 'coordinates' field for LineString geometry." }
        val coordinates = coordinatesNode.map { parsePosition(it) }

        return LineString(coordinates)
    }
}
