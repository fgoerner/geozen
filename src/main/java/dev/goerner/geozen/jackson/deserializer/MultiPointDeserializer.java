package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MultiPointDeserializer extends AbstractGeometryDeserializer<MultiPoint> {

    @Override
	public MultiPoint deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = p.readValueAsTree();

		checkType(rootNode, "MultiPoint");

		List<Position> coordinates = new ArrayList<>();
		for (JsonNode coordinateNode : rootNode.get("coordinates")) {
			coordinates.add(parsePosition(coordinateNode));
		}

		return new MultiPoint(coordinates);
	}
}
