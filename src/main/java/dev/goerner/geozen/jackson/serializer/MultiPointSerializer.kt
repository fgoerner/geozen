package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

public class MultiPointSerializer extends AbstractGeometrySerializer<MultiPoint> {


    @Override
    public void serialize(MultiPoint value, JsonGenerator gen, SerializationContext ctxt) {
        gen.writeStartObject();

        gen.writeStringProperty("type", "MultiPoint");

        gen.writeArrayPropertyStart("coordinates");
        for (Position position : value.getCoordinates()) {
            writePosition(position, gen);
        }
        gen.writeEndArray();

        gen.writeEndObject();
    }
}
