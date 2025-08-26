package dev.goerner.geozen.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;

public class LineStringSerializer extends AbstractGeometrySerializer<LineString> {

	@Override
	public void serialize(LineString lineString, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "LineString");

		gen.writeArrayFieldStart("coordinates");
		for (Position position : lineString.getCoordinates()) {
			writePosition(position, gen);
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
