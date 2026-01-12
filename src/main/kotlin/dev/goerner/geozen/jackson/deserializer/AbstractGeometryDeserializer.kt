package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer

abstract class AbstractGeometryDeserializer<T : Geometry> : ValueDeserializer<T>() {

    protected fun checkType(rootNode: JsonNode, expectedType: String) {
        val typeNode = rootNode["type"]
        require(typeNode != null && typeNode.isString) { "Missing or invalid 'type' field in GeoJSON object." }
        val type = typeNode.asString()
        require(expectedType == type) { "Invalid GeoJSON type: $type. Expected '$expectedType'." }
    }

    protected fun parsePosition(coordinates: JsonNode): Position {
        require(coordinates.isArray && coordinates.size() >= 2) {
            "Invalid coordinate array: '$coordinates'. Expected at least two elements for longitude and latitude."
        }

        val longitude = coordinates[0].asDouble()
        val latitude = coordinates[1].asDouble()
        val altitude = if (coordinates.size() > 2) coordinates[2].asDouble() else 0.0
        return Position(longitude, latitude, altitude)
    }
}
