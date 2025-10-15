package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class LineStringDeserializer extends AbstractGeometryDeserializer<LineString> {

    @Override
    public LineString deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();

        checkType(rootNode, "LineString");

        List<Position> coordinates = new ArrayList<>();
        for (JsonNode coordinateNode : rootNode.get("coordinates")) {
            coordinates.add(parsePosition(coordinateNode));
        }

        return new LineString(coordinates);
    }
}
