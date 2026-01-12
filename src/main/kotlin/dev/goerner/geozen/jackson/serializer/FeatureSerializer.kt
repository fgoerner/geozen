package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.Feature
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class FeatureSerializer : ValueSerializer<Feature>() {

    override fun serialize(value: Feature, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "Feature")
        value.id?.let { gen.writeStringProperty("id", it) }

        gen.writeName("geometry")
        if (value.geometry != null) {
            gen.writePOJO(value.geometry)
        } else {
            gen.writeNull()
        }

        gen.writeName("properties")
        gen.writePOJO(value.properties)

        gen.writeEndObject()
    }
}
