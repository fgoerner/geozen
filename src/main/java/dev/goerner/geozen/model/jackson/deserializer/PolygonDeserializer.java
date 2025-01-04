package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.Polygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;

public class PolygonDeserializer extends AbstractGeometryDeserializer<Polygon> {

	@Override
	public Polygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		checkType(rootNode, "Polygon");

		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		for (JsonNode ringNode : rootNode.get("coordinates")) {
			ArrayList<Position> ring = new ArrayList<>();
			for (JsonNode coordinateNode : ringNode) {
				ring.add(parsePosition(coordinateNode));
			}
			coordinates.add(ring);
		}

		return new Polygon(coordinates);
	}
}
