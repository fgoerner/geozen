package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class LineStringDeserializer extends AbstractGeometryDeserializer<LineString> {

    public LineStringDeserializer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
	public LineString deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = objectMapper.readTree(p);

		checkType(rootNode, "LineString");

		List<Position> coordinates = new ArrayList<>();
		for (JsonNode coordinateNode : rootNode.get("coordinates")) {
			coordinates.add(parsePosition(coordinateNode));
		}

		return new LineString(coordinates);
	}
}
