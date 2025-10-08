package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.collections.FeatureCollection;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class FeatureCollectionSerializer extends ValueSerializer<FeatureCollection> {

	@Override
	public void serialize(FeatureCollection value, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "FeatureCollection");

		gen.writeArrayPropertyStart("features");
		ValueSerializer<Object> featureSerializer = ctxt.findValueSerializer(Feature.class);
		for (Feature feature : value.getFeatures()) {
			featureSerializer.serialize(feature, gen, ctxt);
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
