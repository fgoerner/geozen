package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.collections.FeatureCollection
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class FeatureCollectionSerializer : ValueSerializer<FeatureCollection>() {

    override fun serialize(value: FeatureCollection, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "FeatureCollection")

        gen.writeArrayPropertyStart("features")
        val featureSerializer = ctxt.findValueSerializer(Feature::class.java)
        for (feature in value.features) {
            featureSerializer.serialize(feature, gen, ctxt)
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
