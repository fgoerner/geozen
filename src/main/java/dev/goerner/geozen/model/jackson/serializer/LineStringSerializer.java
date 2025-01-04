package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;

public class LineStringSerializer extends JsonSerializer<LineString> {

	@Override
	public void serialize(LineString lineString, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "LineString");

		gen.writeArrayFieldStart("coordinates");
		for (Position position : lineString.getCoordinates()) {
			gen.writeStartArray();
			gen.writeNumber(position.getLongitude());
			gen.writeNumber(position.getLatitude());
			if (position.getAltitude() != 0) {
				gen.writeNumber(position.getAltitude());
			}
			gen.writeEndArray();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
