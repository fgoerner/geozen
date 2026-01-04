package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.multi_geometry.MultiPoint
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class MultiPointDeserializer : AbstractGeometryDeserializer<MultiPoint>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiPoint {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "MultiPoint")

        val coordinatesNode = rootNode["coordinates"]
        require(coordinatesNode != null && coordinatesNode.isArray) { "Invalid or missing 'coordinates' field for MultiPoint geometry." }
        val coordinates = coordinatesNode.map { parsePosition(it) }

        return MultiPoint(coordinates)
    }
}
