package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

import java.util.List;

public class MultiPolygonSerializer extends AbstractGeometrySerializer<MultiPolygon> {

	@Override
	public void serialize(MultiPolygon value, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "MultiPolygon");

		gen.writeArrayPropertyStart("coordinates");
		for (List<List<Position>> polygon : value.getCoordinates()) {
			gen.writeStartArray();
			for (List<Position> ring : polygon) {
				gen.writeStartArray();
				for (Position position : ring) {
					writePosition(position, gen);
				}
				gen.writeEndArray();
			}
			gen.writeEndArray();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
