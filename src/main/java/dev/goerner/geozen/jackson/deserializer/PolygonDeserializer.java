package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.simple_geometry.Polygon;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class PolygonDeserializer extends AbstractGeometryDeserializer<Polygon> {

    public PolygonDeserializer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
	public Polygon deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = objectMapper.readTree(p);

		checkType(rootNode, "Polygon");

        List<List<Position>> coordinates = new ArrayList<>();
		for (JsonNode ringNode : rootNode.get("coordinates")) {
            List<Position> ring = new ArrayList<>();
			for (JsonNode coordinateNode : ringNode) {
				ring.add(parsePosition(coordinateNode));
			}
			coordinates.add(ring);
		}

		return new Polygon(coordinates);
	}
}
