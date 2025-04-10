package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;

public class LineStringDeserializer extends AbstractGeometryDeserializer<LineString> {

	@Override
	public LineString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		checkType(rootNode, "LineString");

		ArrayList<Position> coordinates = new ArrayList<>();
		for (JsonNode coordinateNode : rootNode.get("coordinates")) {
			coordinates.add(parsePosition(coordinateNode));
		}

		return new LineString(coordinates);
	}
}
