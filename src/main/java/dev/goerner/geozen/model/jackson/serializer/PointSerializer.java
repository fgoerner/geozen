package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.Point;

import java.io.IOException;

public class PointSerializer extends JsonSerializer<Point> {

	@Override
	public void serialize(Point point, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "Point");

		gen.writeArrayFieldStart("coordinates");
		gen.writeNumber(point.getLongitude());
		gen.writeNumber(point.getLatitude());
		if (point.getAltitude() != 0) {
			gen.writeNumber(point.getAltitude());
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
