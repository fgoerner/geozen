package dev.goerner.geozen.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonDeserializer extends AbstractGeometryDeserializer<Polygon> {

	@Override
	public Polygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

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
