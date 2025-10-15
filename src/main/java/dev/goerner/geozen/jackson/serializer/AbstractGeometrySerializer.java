package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.ValueSerializer;

public abstract class AbstractGeometrySerializer<T extends Geometry> extends ValueSerializer<T> {

    protected void writePosition(Position position, JsonGenerator gen) {
        gen.writeStartArray();
        gen.writeNumber(position.getLongitude());
        gen.writeNumber(position.getLatitude());
        if (position.getAltitude() != 0) {
            gen.writeNumber(position.getAltitude());
        }
        gen.writeEndArray();
    }
}
