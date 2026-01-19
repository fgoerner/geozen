package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.multi_geometry.MultiPoint
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class MultiPointSerializer : AbstractGeometrySerializer<MultiPoint>() {

    override fun serialize(value: MultiPoint, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "MultiPoint")

        gen.writeArrayPropertyStart("coordinates")
        for (position in value.coordinates) {
            writePosition(position, gen)
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
