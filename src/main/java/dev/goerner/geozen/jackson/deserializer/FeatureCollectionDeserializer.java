package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.collections.FeatureCollection;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class FeatureCollectionDeserializer extends ValueDeserializer<FeatureCollection> {

    @Override
	public FeatureCollection deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = p.readValueAsTree();

		String type = rootNode.get("type").asString();
		if (!type.equalsIgnoreCase("FeatureCollection")) {
			throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'FeatureCollection'.");
		}

		ValueDeserializer<?> featureDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Feature.class));

		List<Feature> features = new ArrayList<>();
		for (JsonNode featureNode : rootNode.get("features")) {
			features.add((Feature) featureDeserializer.deserialize(featureNode.traverse(ctxt), ctxt));
		}

		return new FeatureCollection(features);
	}
}
