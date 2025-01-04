package dev.goerner.geozen.model.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.MultiLineString;
import dev.goerner.geozen.model.MultiPoint;
import dev.goerner.geozen.model.MultiPolygon;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Polygon;

import java.io.IOException;

public class GeometryDeserializer extends AbstractGeometryDeserializer<Geometry> {

	@Override
	public Geometry deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);
		String type = rootNode.get("type").asText();

		JavaType javaType = switch (type) {
			case "Point" -> ctxt.constructType(Point.class);
			case "LineString" -> ctxt.constructType(LineString.class);
			case "Polygon" -> ctxt.constructType(Polygon.class);
			case "MultiPoint" -> ctxt.constructType(MultiPoint.class);
			case "MultiLineString" -> ctxt.constructType(MultiLineString.class);
			case "MultiPolygon" -> ctxt.constructType(MultiPolygon.class);
			default -> throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ".");
		};
		JsonDeserializer<?> deserializer = ctxt.findRootValueDeserializer(javaType);

		return (Geometry) deserializer.deserialize(rootNode.traverse(p.getCodec()), ctxt);
	}
}
