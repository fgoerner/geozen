package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.multi_geometry.MultiLineString
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class MultiLineStringSerializer : AbstractGeometrySerializer<MultiLineString>() {

    override fun serialize(value: MultiLineString, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "MultiLineString")

        gen.writeArrayPropertyStart("coordinates")
        for (lineString in value.coordinates) {
            gen.writeStartArray()
            for (position in lineString) {
                writePosition(position, gen)
            }
            gen.writeEndArray()
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
