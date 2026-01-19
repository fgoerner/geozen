package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class GeometryDeserializer : AbstractGeometryDeserializer<Geometry>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Geometry {
        val rootNode = p.readValueAsTree<JsonNode>()

        val typeNode = rootNode["type"]
        require(typeNode != null && typeNode.isString) { "Geometry must have a valid 'type' field." }

        val typeClass = when (val type = typeNode.asString()) {
            "Point" -> Point::class.java
            "LineString" -> LineString::class.java
            "Polygon" -> Polygon::class.java
            "MultiPoint" -> MultiPoint::class.java
            "MultiLineString" -> MultiLineString::class.java
            "MultiPolygon" -> MultiPolygon::class.java
            "GeometryCollection" -> GeometryCollection::class.java
            else -> throw IllegalArgumentException("Invalid GeoJSON type: $type.")
        }

        return ctxt.readValue(rootNode.traverse(ctxt), typeClass)
    }
}
