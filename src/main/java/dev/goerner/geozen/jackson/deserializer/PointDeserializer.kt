package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.simple_geometry.Point;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;

public class PointDeserializer extends AbstractGeometryDeserializer<Point> {

    @Override
    public Point deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();

        checkType(rootNode, "Point");

        return new Point(parsePosition(rootNode.get("coordinates")));
    }
}
