package dev.goerner.geozen.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.simple_geometry.Point;

import java.io.IOException;

public class PointSerializer extends AbstractGeometrySerializer<Point> {

	@Override
	public void serialize(Point point, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "Point");

		gen.writeFieldName("coordinates");
		writePosition(point.getCoordinates(), gen);

		gen.writeEndObject();
	}
}
