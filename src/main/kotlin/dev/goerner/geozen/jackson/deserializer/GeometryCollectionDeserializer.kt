package dev.goerner.geozen.jackson.deserializer

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.collections.GeometryCollection
import tools.jackson.core.JsonParser
import tools.jackson.databind.DeserializationContext
import tools.jackson.databind.JsonNode

class GeometryCollectionDeserializer : AbstractGeometryDeserializer<GeometryCollection>() {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): GeometryCollection {
        val rootNode = p.readValueAsTree<JsonNode>()

        checkType(rootNode, "GeometryCollection")

        val geometriesNode = rootNode["geometries"]
        require(geometriesNode != null && geometriesNode.isArray) { "Invalid or missing 'geometries' field for GeometryCollection." }

        val geometries = geometriesNode.map {
            ctxt.readValue(it.traverse(ctxt), Geometry::class.java)
        }

        return GeometryCollection(geometries)
    }
}

