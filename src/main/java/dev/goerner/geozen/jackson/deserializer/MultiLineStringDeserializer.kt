package dev.goerner.geozen.jackson.deserializer;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class MultiLineStringDeserializer extends AbstractGeometryDeserializer<MultiLineString> {

    @Override
    public MultiLineString deserialize(JsonParser p, DeserializationContext ctxt) {
        JsonNode rootNode = p.readValueAsTree();

        checkType(rootNode, "MultiLineString");

        List<List<Position>> coordinates = new ArrayList<>();
        for (JsonNode lineStringNode : rootNode.get("coordinates")) {
            List<Position> lineString = new ArrayList<>();
            for (JsonNode coordinateNode : lineStringNode) {
                lineString.add(parsePosition(coordinateNode));
            }
            coordinates.add(lineString);
        }

        return new MultiLineString(coordinates);
    }
}
