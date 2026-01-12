package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.simple_geometry.Point
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext

class PointSerializer : AbstractGeometrySerializer<Point>() {

    override fun serialize(point: Point, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "Point")

        gen.writeName("coordinates")
        writePosition(point.coordinates, gen)

        gen.writeEndObject()
    }
}
