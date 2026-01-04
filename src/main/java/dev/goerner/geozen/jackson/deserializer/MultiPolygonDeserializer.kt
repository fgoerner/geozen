package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class MultiPolygonDeserializer : AbstractGeometryDeserializer<MultiPolygon>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiPolygon {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "MultiPolygon")

        val coordinates =
            rootNode["coordinates"].map { polygonNode -> polygonNode.map { ringNode -> ringNode.map { parsePosition(it) } } }

        return MultiPolygon(coordinates)
    }
}
