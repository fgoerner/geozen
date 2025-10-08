package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

import java.util.List;

public class MultiLineStringSerializer extends AbstractGeometrySerializer<MultiLineString> {

	@Override
	public void serialize(MultiLineString value, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "MultiLineString");

		gen.writeArrayPropertyStart("coordinates");
		for (List<Position> lineString : value.getCoordinates()) {
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
