package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.Position;

import java.io.IOException;

public abstract class AbstractGeometrySerializer<T extends Geometry> extends JsonSerializer<T> {

	protected void writePosition(Position position, JsonGenerator gen) throws IOException {
		gen.writeStartArray();
		gen.writeNumber(position.getLongitude());
		gen.writeNumber(position.getLatitude());
		if (position.getAltitude() != 0) {
			gen.writeNumber(position.getAltitude());
		}
		gen.writeEndArray();
	}
}
