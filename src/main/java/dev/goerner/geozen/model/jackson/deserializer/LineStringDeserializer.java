package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LineStringDeserializer extends AbstractGeometryDeserializer<LineString> {

	@Override
	public LineString deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		checkType(rootNode, "LineString");

		ArrayList<Position> coordinates = StreamSupport.stream(rootNode.get("coordinates").spliterator(), false)
																	  .map(this::parsePosition)
																	  .collect(Collectors.toCollection(ArrayList::new));

		return new LineString(coordinates);
	}
}
