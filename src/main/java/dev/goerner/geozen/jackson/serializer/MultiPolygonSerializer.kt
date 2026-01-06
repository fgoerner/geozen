package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class MultiPolygonSerializer : AbstractGeometrySerializer<MultiPolygon>() {

    override fun serialize(value: MultiPolygon, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "MultiPolygon")

        gen.writeArrayPropertyStart("coordinates")
        for (polygon in value.coordinates) {
            gen.writeStartArray()
            for (ring in polygon) {
                gen.writeStartArray()
                for (position in ring) {
                    writePosition(position, gen)
                }
                gen.writeEndArray()
            }
            gen.writeEndArray()
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
