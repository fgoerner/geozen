package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.MultiPolygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;

public class MultiPolygonDeserializer extends AbstractGeometryDeserializer<MultiPolygon> {

	@Override
	public MultiPolygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		checkType(rootNode, "MultiPolygon");

		ArrayList<ArrayList<ArrayList<Position>>> coordinates = new ArrayList<>();
		for (JsonNode polygonNode : rootNode.get("coordinates")) {
			ArrayList<ArrayList<Position>> polygon = new ArrayList<>();
			for (JsonNode ringNode : polygonNode) {
				ArrayList<Position> ring = new ArrayList<>();
				for (JsonNode coordinateNode : ringNode) {
					ring.add(parsePosition(coordinateNode));
				}
				polygon.add(ring);
			}
			coordinates.add(polygon);
		}

		return new MultiPolygon(coordinates);
	}
}
