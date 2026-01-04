package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import java.util.ArrayList;
import java.util.List;

public class GeometryCollectionDeserializer extends ValueDeserializer<GeometryCollection> {

    @Override
    public GeometryCollection deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();

        String type = rootNode.get("type").asString();
        if (!type.equalsIgnoreCase("GeometryCollection")) {
            throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ". Expected 'GeometryCollection'.");
        }

        ValueDeserializer<?> pointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Point.class));
        ValueDeserializer<?> lineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(LineString.class));
        ValueDeserializer<?> polygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(Polygon.class));
        ValueDeserializer<?> multiPointSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPoint.class));
        ValueDeserializer<?> multiLineStringSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiLineString.class));
        ValueDeserializer<?> multiPolygonSerializer = ctxt.findRootValueDeserializer(ctxt.constructType(MultiPolygon.class));

        List<Geometry> geometries = new ArrayList<>();
        for (JsonNode geometryNode : rootNode.get("geometries")) {
            type = geometryNode.get("type").asString();
            ValueDeserializer<?> geometryDeserializer = switch (type) {
                case "Point" -> pointSerializer;
                case "LineString" -> lineStringSerializer;
                case "Polygon" -> polygonSerializer;
                case "MultiPoint" -> multiPointSerializer;
                case "MultiLineString" -> multiLineStringSerializer;
                case "MultiPolygon" -> multiPolygonSerializer;
                default -> throw new IllegalArgumentException("Invalid GeoJSON type: " + type + ".");
            };
            geometries.add((Geometry) geometryDeserializer.deserialize(geometryNode.traverse(ctxt), ctxt));
        }

        return new GeometryCollection(geometries);
    }
}
