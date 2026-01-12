package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.collections.FeatureCollection
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class FeatureCollectionSerializer : ValueSerializer<FeatureCollection>() {

    override fun serialize(value: FeatureCollection, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "FeatureCollection")

        gen.writeArrayPropertyStart("features")
        for (feature in value.features) {
            gen.writePOJO(feature)
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
