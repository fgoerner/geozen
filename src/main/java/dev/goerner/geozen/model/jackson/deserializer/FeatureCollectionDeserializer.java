package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.FeatureCollection;

import java.io.IOException;
import java.util.ArrayList;

public class FeatureCollectionDeserializer extends JsonDeserializer<FeatureCollection> {

	@Override
	public FeatureCollection deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		String type = rootNode.get("type").asText();
		if (!type.equalsIgnoreCase("FeatureCollection")) {
			throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'FeatureCollection'.");
		}

		JsonDeserializer<?> featureDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Feature.class));

		ArrayList<Feature> features = new ArrayList<>();
		for (JsonNode featureNode : rootNode.get("features")) {
			features.add((Feature) featureDeserializer.deserialize(featureNode.traverse(p.getCodec()), ctxt));
		}

		return new FeatureCollection(features);
	}
}
