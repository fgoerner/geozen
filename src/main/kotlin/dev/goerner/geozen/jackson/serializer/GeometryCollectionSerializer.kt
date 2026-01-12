package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.collections.GeometryCollection
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class GeometryCollectionSerializer : ValueSerializer<GeometryCollection>() {

    override fun serialize(value: GeometryCollection, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "GeometryCollection")

        gen.writeArrayPropertyStart("geometries")
        for (geometry in value.geometries) {
            gen.writePOJO(geometry)
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
