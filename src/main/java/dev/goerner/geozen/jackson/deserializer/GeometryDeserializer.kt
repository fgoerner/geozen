package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

public class GeometryDeserializer extends AbstractGeometryDeserializer<Geometry> {

    @Override
    public Geometry deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();
        String type = rootNode.get("type").asString();

        JavaType javaType = switch (type) {
            case "Point" -> ctxt.constructType(Point.class);
            case "LineString" -> ctxt.constructType(LineString.class);
            case "Polygon" -> ctxt.constructType(Polygon.class);
            case "MultiPoint" -> ctxt.constructType(MultiPoint.class);
            case "MultiLineString" -> ctxt.constructType(MultiLineString.class);
            case "MultiPolygon" -> ctxt.constructType(MultiPolygon.class);
            default -> throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ".");
        };
        ValueDeserializer<?> deserializer = ctxt.findRootValueDeserializer(javaType);

        return (Geometry) deserializer.deserialize(rootNode.traverse(ctxt), ctxt);
    }
}
