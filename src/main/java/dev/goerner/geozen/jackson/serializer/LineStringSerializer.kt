package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.simple_geometry.LineString
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class LineStringSerializer : AbstractGeometrySerializer<LineString>() {

    override fun serialize(lineString: LineString, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "LineString")

        gen.writeArrayPropertyStart("coordinates")
        for (position in lineString.coordinates) {
            writePosition(position, gen)
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
