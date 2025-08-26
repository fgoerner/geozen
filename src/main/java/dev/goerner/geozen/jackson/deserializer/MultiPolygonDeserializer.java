package dev.goerner.geozen.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MultiPolygonDeserializer extends AbstractGeometryDeserializer<MultiPolygon> {

	@Override
	public MultiPolygon deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

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
