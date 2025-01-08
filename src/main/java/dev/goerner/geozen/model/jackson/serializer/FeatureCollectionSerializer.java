package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.FeatureCollection;

import java.io.IOException;

public class FeatureCollectionSerializer extends JsonSerializer<FeatureCollection> {

	@Override
	public void serialize(FeatureCollection value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "FeatureCollection");

		gen.writeArrayFieldStart("features");
		JsonSerializer<Object> featureSerializer = serializers.findValueSerializer(Feature.class);
		for (Feature feature : value.getFeatures()) {
			featureSerializer.serialize(feature, gen, serializers);
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
