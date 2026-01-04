package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.simple_geometry.Point;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

public class PointSerializer extends AbstractGeometrySerializer<Point> {

    @Override
    public void serialize(Point point, JsonGenerator gen, SerializationContext ctxt) {
        gen.writeStartObject();

        gen.writeStringProperty("type", "Point");

        gen.writeName("coordinates");
        writePosition(point.getCoordinates(), gen);

        gen.writeEndObject();
    }
}
