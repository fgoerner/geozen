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
import tools.jackson.databind.ValueDeserializer

class GeometryCollectionDeserializer : ValueDeserializer<GeometryCollection>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GeometryCollection {
        val rootNode = p.readValueAsTree<JsonNode>()

        val typeNode = rootNode["type"]
        require(typeNode != null && typeNode.isString) { "GeometryCollection must have a valid 'type' field." }
        val type = typeNode.asString()
        require(
            type.equals(
                "GeometryCollection",
                ignoreCase = true
            )
        ) { "Invalid GeoJSON type: $type. Expected 'GeometryCollection'." }

        val pointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Point::class.java))
        val lineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(LineString::class.java))
        val polygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Polygon::class.java))
        val multiPointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPoint::class.java))
        val multiLineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiLineString::class.java))
        val multiPolygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPolygon::class.java))

        val geometriesNode = rootNode["geometries"]
        require(geometriesNode != null && geometriesNode.isArray) { "Invalid or missing 'geometries' field for GeometryCollection." }
        val geometries = geometriesNode.map {
            val localTypeNode = it["type"]
            require(localTypeNode != null && localTypeNode.isString) { "Each geometry in 'geometries' must have a valid 'type' field." }
            val geometryDeserializer = when (val localType = localTypeNode.asString()) {
                "Point" -> pointSerializer
                "LineString" -> lineStringSerializer
                "Polygon" -> polygonSerializer
                "MultiPoint" -> multiPointSerializer
                "MultiLineString" -> multiLineStringSerializer
                "MultiPolygon" -> multiPolygonSerializer
                else -> throw java.lang.IllegalArgumentException("Invalid GeoJSON type: $localType.")
            }

            geometryDeserializer.deserialize(it.traverse(ctxt), ctxt) as Geometry
        }

        return GeometryCollection(geometries)
    }
}
