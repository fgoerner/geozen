package dev.goerner.geozen.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.Position;

import java.io.IOException;
import java.util.List;

public class MultiPolygonSerializer extends AbstractGeometrySerializer<MultiPolygon> {

	@Override
	public void serialize(MultiPolygon value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "MultiPolygon");

		gen.writeArrayFieldStart("coordinates");
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
