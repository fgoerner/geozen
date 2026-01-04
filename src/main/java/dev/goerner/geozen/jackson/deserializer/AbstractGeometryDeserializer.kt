package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import tools.jackson.databind.JsonNode
import tools.jackson.databind.ValueDeserializer

abstract class AbstractGeometryDeserializer<T : Geometry> : ValueDeserializer<T>() {

    protected fun checkType(rootNode: JsonNode, expectedType: String) {
        val type = rootNode["type"].asString()
        require(
            expectedType.equals(type, ignoreCase = true)
        ) { "Invalid GeoJSON type: $type. Expected '$expectedType'." }
    }

    protected fun parsePosition(coordinates: JsonNode): Position {
        val longitude = coordinates[0].asDouble()
        val latitude = coordinates[1].asDouble()
        var altitude = 0.0
        if (coordinates.size() > 2) {
            altitude = coordinates[2].asDouble()
        }
        return Position(longitude, latitude, altitude)
    }
}
