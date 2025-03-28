package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.MultiLineString;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;

public class MultiLineStringSerializer extends AbstractGeometrySerializer<MultiLineString> {

	@Override
	public void serialize(MultiLineString value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "MultiLineString");

		gen.writeArrayFieldStart("coordinates");
		for (ArrayList<Position> lineString : value.getCoordinates()) {
			gen.writeStartArray();
			for (Position position : lineString) {
				writePosition(position, gen);
			}
			gen.writeEndArray();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
