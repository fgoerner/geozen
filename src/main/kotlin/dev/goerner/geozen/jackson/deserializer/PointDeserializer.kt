package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.simple_geometry.Point
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class PointDeserializer : AbstractGeometryDeserializer<Point>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Point {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "Point")

        val coordinates = rootNode["coordinates"]
        require(coordinates != null) { "Missing 'coordinates' field" }

        return Point(parsePosition(coordinates))
    }
}
