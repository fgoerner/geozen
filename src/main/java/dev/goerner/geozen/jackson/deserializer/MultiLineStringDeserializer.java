package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class MultiLineStringDeserializer extends AbstractGeometryDeserializer<MultiLineString> {

    public MultiLineStringDeserializer(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
	public MultiLineString deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = objectMapper.readTree(p);

		checkType(rootNode, "MultiLineString");

		List<List<Position>> coordinates = new ArrayList<>();
		for (JsonNode lineStringNode : rootNode.get("coordinates")) {
			List<Position> lineString = new ArrayList<>();
			for (JsonNode coordinateNode : lineStringNode) {
				lineString.add(parsePosition(coordinateNode));
			}
			coordinates.add(lineString);
		}

		return new MultiLineString(coordinates);
	}
}
