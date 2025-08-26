package dev.goerner.geozen.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiLineStringDeserializer extends AbstractGeometryDeserializer<MultiLineString> {

	@Override
	public MultiLineString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

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
