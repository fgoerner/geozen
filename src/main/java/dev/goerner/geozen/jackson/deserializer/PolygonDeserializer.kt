package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class PolygonDeserializer : AbstractGeometryDeserializer<Polygon>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Polygon {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "Polygon")

        val coordinates = rootNode["coordinates"].map { ringNode -> ringNode.map { parsePosition(it) } }

        return Polygon(coordinates)
    }
}
