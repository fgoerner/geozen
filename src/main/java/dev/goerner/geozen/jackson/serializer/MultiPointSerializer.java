package dev.goerner.geozen.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.Position;

import java.io.IOException;

public class MultiPointSerializer extends AbstractGeometrySerializer<MultiPoint> {


	@Override
	public void serialize(MultiPoint value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "MultiPoint");

		gen.writeArrayFieldStart("coordinates");
		for (Position position : value.getCoordinates()) {
			writePosition(position, gen);
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
