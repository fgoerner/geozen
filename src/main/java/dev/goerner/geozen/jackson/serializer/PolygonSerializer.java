package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.simple_geometry.Polygon;
import dev.goerner.geozen.model.Position;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;

import java.util.List;

public class PolygonSerializer extends AbstractGeometrySerializer<Polygon> {

	@Override
	public void serialize(Polygon polygon, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "Polygon");

		gen.writeArrayPropertyStart("coordinates");
		for (List<Position> ring : polygon.getCoordinates()) {
			gen.writeStartArray();
			for (Position position : ring) {
				writePosition(position, gen);
			}
			gen.writeEndArray();
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
