package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

public abstract class AbstractGeometryDeserializer<T extends Geometry> extends ValueDeserializer<T> {

    protected void checkType(JsonNode rootNode, String expectedType) {
		String type = rootNode.get("type").asString();
		if (!expectedType.equalsIgnoreCase(type)) {
			throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected '" + expectedType + "'.");
		}
	}

	protected Position parsePosition(JsonNode coordinates) {
		double longitude = coordinates.get(0).asDouble();
		double latitude = coordinates.get(1).asDouble();
		double altitude = 0;
		if (coordinates.size() > 2) {
			altitude = coordinates.get(2).asDouble();
		}
		return new Position(longitude, latitude, altitude);
	}
}
