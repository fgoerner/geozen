package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MultiPolygonDeserializer extends AbstractGeometryDeserializer<MultiPolygon> {

    @Override
	public MultiPolygon deserialize(JsonParser p, DeserializationContext ctxt) {
		JsonNode rootNode = p.readValueAsTree();

		checkType(rootNode, "MultiPolygon");

		List<List<List<Position>>> coordinates = new ArrayList<>();
		for (JsonNode polygonNode : rootNode.get("coordinates")) {
            List<List<Position>> polygon = new ArrayList<>();
			for (JsonNode ringNode : polygonNode) {
                List<Position> ring = new ArrayList<>();
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
