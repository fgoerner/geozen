package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.ValueSerializer

abstract class AbstractGeometrySerializer<T : Geometry> : ValueSerializer<T>() {

    protected fun writePosition(position: Position, gen: JsonGenerator) {
        gen.writeStartArray()
        gen.writeNumber(position.longitude)
        gen.writeNumber(position.latitude)
        if (position.altitude != 0.0) {
            gen.writeNumber(position.altitude)
        }
        gen.writeEndArray()
    }
}
