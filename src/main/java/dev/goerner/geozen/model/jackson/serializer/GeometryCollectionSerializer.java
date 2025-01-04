package dev.goerner.geozen.model.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.GeometryCollection;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.MultiLineString;
import dev.goerner.geozen.model.MultiPoint;
import dev.goerner.geozen.model.MultiPolygon;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Polygon;

import java.io.IOException;

public class GeometryCollectionSerializer extends JsonSerializer<GeometryCollection> {

	@Override
	public void serialize(GeometryCollection value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		gen.writeStartObject();

		gen.writeStringField("type", "GeometryCollection");

		gen.writeArrayFieldStart("geometries");
		for (Geometry geometry : value.getGeometries()) {
			switch (geometry) {
				case Point point -> serializers.findValueSerializer(Point.class).serialize(point, gen, serializers);
				case LineString lineString -> serializers.findValueSerializer(LineString.class).serialize(lineString, gen, serializers);
				case Polygon polygon -> serializers.findValueSerializer(Polygon.class).serialize(polygon, gen, serializers);
				case MultiPoint multiPoint -> serializers.findValueSerializer(MultiPoint.class).serialize(multiPoint, gen, serializers);
				case MultiLineString multiLineString -> serializers.findValueSerializer(MultiLineString.class).serialize(multiLineString, gen, serializers);
				case MultiPolygon multiPolygon -> serializers.findValueSerializer(MultiPolygon.class).serialize(multiPolygon, gen, serializers);
				default -> throw new IllegalArgumentException("Invalid Geometry type: " + geometry.getClass().getSimpleName());
			}
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
