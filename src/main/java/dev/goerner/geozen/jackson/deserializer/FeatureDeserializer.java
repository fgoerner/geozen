package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.Geometry;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.HashMap;
import java.util.Map;

public class FeatureDeserializer extends ValueDeserializer<Feature> {

    @Override
    public Feature deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();

        String type = rootNode.get("type").asString();
        if (!type.equalsIgnoreCase("Feature")) {
            throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'Feature'.");
        }

        String id = rootNode.get("id").asString();

        ValueDeserializer<?> geometryDeserializer = ctxt.findRootValueDeserializer(ctxt.constructType(Geometry.class));
        Geometry geometry = (Geometry) geometryDeserializer.deserialize(rootNode.get("geometry").traverse(ctxt), ctxt);

        Map<String, String> properties = new HashMap<>();
        for (Map.Entry<String, JsonNode> property : rootNode.get("properties").properties()) {
            properties.put(property.getKey(), property.getValue().asString());
        }

        return new Feature(id, geometry, properties);
    }
}
