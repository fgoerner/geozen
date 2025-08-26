package dev.goerner.geozen.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeometryCollectionDeserializer extends JsonDeserializer<GeometryCollection> {

	@Override
	public GeometryCollection deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode rootNode = p.getCodec().readTree(p);

		String type = rootNode.get("type").asText();
		if (!type.equalsIgnoreCase("GeometryCollection")) {
			throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'GeometryCollection'.");
		}

		JsonDeserializer<?> pointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Point.class));
		JsonDeserializer<?> lineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(LineString.class));
		JsonDeserializer<?> polygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Polygon.class));
		JsonDeserializer<?> multiPointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPoint.class));
		JsonDeserializer<?> multiLineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiLineString.class));
		JsonDeserializer<?> multiPolygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPolygon.class));

		List<Geometry> geometries = new ArrayList<>();
		for (JsonNode geometryNode : rootNode.get("geometries")) {
			type = geometryNode.get("type").asText();
			JsonDeserializer<?> geometryDeserializer = switch (type) {
				case "Point" -> pointSerializer;
				case "LineString" -> lineStringSerializer;
				case "Polygon" -> polygonSerializer;
				case "MultiPoint" -> multiPointSerializer;
				case "MultiLineString" -> multiLineStringSerializer;
				case "MultiPolygon" -> multiPolygonSerializer;
				default -> throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ".");
			};
			geometries.add((Geometry) geometryDeserializer.deserialize(geometryNode.traverse(p.getCodec()), ctxt));
		}

		return new GeometryCollection(geometries);
	}
}
