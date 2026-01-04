package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.simple_geometry.LineString;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

public class LineStringSerializer extends AbstractGeometrySerializer<LineString> {

    @Override
    public void serialize(LineString lineString, JsonGenerator gen, SerializationContext ctxt) {
        gen.writeStartObject();

        gen.writeStringProperty("type", "LineString");

        gen.writeArrayPropertyStart("coordinates");
        for (Position position : lineString.getCoordinates()) {
            writePosition(position, gen);
        }
        gen.writeEndArray();

        gen.writeEndObject();
    }
}
