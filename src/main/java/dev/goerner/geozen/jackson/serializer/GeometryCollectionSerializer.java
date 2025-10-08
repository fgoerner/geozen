package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class GeometryCollectionSerializer extends ValueSerializer<GeometryCollection> {

	@Override
	public void serialize(GeometryCollection value, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "GeometryCollection");

		gen.writeArrayPropertyStart("geometries");
		for (Geometry geometry : value.getGeometries()) {
			switch (geometry) {
				case Point point -> ctxt.findValueSerializer(Point.class).serialize(point, gen, ctxt);
				case LineString lineString -> ctxt.findValueSerializer(LineString.class).serialize(lineString, gen, ctxt);
				case Polygon polygon -> ctxt.findValueSerializer(Polygon.class).serialize(polygon, gen, ctxt);
				case MultiPoint multiPoint -> ctxt.findValueSerializer(MultiPoint.class).serialize(multiPoint, gen, ctxt);
				case MultiLineString multiLineString -> ctxt.findValueSerializer(MultiLineString.class).serialize(multiLineString, gen, ctxt);
				case MultiPolygon multiPolygon -> ctxt.findValueSerializer(MultiPolygon.class).serialize(multiPolygon, gen, ctxt);
				default -> throw new IllegalArgumentException("Invalid Geometry type: " + geometry.getClass().getSimpleName());
			}
		}
		gen.writeEndArray();

		gen.writeEndObject();
	}
}
