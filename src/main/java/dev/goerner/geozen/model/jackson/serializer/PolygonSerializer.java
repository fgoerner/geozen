package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.Polygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.ArrayList;

public class PolygonSerializer extends AbstractGeometrySerializer<Polygon> {

	@Override
	public void serialize(Polygon polygon, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "Polygon");

		gen.writeArrayFieldStart("coordinates");
		for (ArrayList<Position> ring : polygon.getCoordinates()) {
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
