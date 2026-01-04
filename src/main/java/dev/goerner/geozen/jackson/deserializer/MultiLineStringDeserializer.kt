package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.multi_geometry.MultiLineString
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class MultiLineStringDeserializer : AbstractGeometryDeserializer<MultiLineString>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiLineString {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "MultiLineString")

        val coordinates = rootNode["coordinates"].map { lineStringNode -> lineStringNode.map { parsePosition(it) } }

        return MultiLineString(coordinates)
    }
}
