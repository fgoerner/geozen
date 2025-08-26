package dev.goerner.geozen.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.simple_geometry.Point;

import java.io.IOException;

public class PointDeserializer extends AbstractGeometryDeserializer<Point> {

	@Override
	public Point deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		checkType(rootNode, "Point");

		return new Point(parsePosition(rootNode.get("coordinates")));
	}
}
