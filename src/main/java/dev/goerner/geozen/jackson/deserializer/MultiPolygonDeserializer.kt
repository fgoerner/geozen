package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class MultiPolygonDeserializer : AbstractGeometryDeserializer<MultiPolygon>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): MultiPolygon {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "MultiPolygon")

        val coordinatesNode = rootNode["coordinates"]
        require(coordinatesNode != null && coordinatesNode.isArray) { "Invalid or missing 'coordinates' field for MultiPolygon geometry." }
        val coordinates = coordinatesNode.map { polygonNode ->
            require(polygonNode != null && polygonNode.isArray) { "Invalid Polygon in 'coordinates' field for MultiPolygon geometry." }
            polygonNode.map { ringNode ->
                require(ringNode != null && ringNode.isArray) { "Invalid linear ring in 'coordinates' field for MultiPolygon geometry." }
                ringNode.map { parsePosition(it) }
            }
        }

        return MultiPolygon(coordinates)
    }
}
