package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.multi_geometry.MultiLineString
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class MultiLineStringDeserializer : AbstractGeometryDeserializer<MultiLineString>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiLineString {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "MultiLineString")

        val coordinatesNode = rootNode["coordinates"]
        require(coordinatesNode != null && coordinatesNode.isArray) { "Invalid or missing 'coordinates' field for MultiLineString geometry." }
        val coordinates = coordinatesNode.map { lineStringNode ->
            require(lineStringNode != null && lineStringNode.isArray) { "Invalid LineString in 'coordinates' field for MultiLineString geometry." }
            lineStringNode.map { parsePosition(it) }
        }

        return MultiLineString(coordinates)
    }
}
