package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class PolygonSerializer : AbstractGeometrySerializer<Polygon>() {

    override fun serialize(polygon: Polygon, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "Polygon")

        gen.writeArrayPropertyStart("coordinates")
        for (ring in polygon.coordinates) {
            gen.writeStartArray()
            for (position in ring) {
                writePosition(position, gen)
            }
            gen.writeEndArray()
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
