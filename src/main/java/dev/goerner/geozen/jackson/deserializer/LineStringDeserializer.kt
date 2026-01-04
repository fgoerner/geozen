package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.simple_geometry.LineString
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class LineStringDeserializer : AbstractGeometryDeserializer<LineString>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LineString {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "LineString")

        val coordinates = rootNode["coordinates"].map { parsePosition(it) }

        return LineString(coordinates)
    }
}
