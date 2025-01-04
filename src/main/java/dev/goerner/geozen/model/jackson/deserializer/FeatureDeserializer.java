package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.Geometry;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FeatureDeserializer extends JsonDeserializer<Feature> {

	@Override
	public Feature deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		String type = rootNode.get("type").asText();
		if (!type.equalsIgnoreCase("Feature")) {
			throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'Feature'.");
		}

		String id = rootNode.get("id").asText();

		JsonDeserializer<?> geometryDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Geometry.class));
		Geometry geometry = (Geometry) geometryDeserializer.deserialize(rootNode.get("geometry").traverse(p.getCodec()), ctxt);

		Map<String, String> properties = new HashMap<>();
		Iterator<Map.Entry<String, JsonNode>> propertiesIterator = rootNode.get("properties").fields();
		while (propertiesIterator.hasNext()) {
			Map.Entry<String, JsonNode> property = propertiesIterator.next();
			properties.put(property.getKey(), property.getValue().asText());
		}

		return new Feature(id, geometry, properties);
	}
}
