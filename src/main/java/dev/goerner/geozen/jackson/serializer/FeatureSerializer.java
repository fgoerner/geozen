package dev.goerner.geozen.jackson.serializer;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;

public class FeatureSerializer extends ValueSerializer<Feature> {

	@Override
	public void serialize(Feature value, JsonGenerator gen, SerializationContext ctxt) {
		gen.writeStartObject();

		gen.writeStringProperty("type", "Feature");

		if (value.getId() != null) {
			gen.writeStringProperty("id", value.getId());
		}

		gen.writeName("geometry");
		switch (value.getGeometry()) {
			case Point point -> ctxt.findValueSerializer(Point.class).serialize(point, gen, ctxt);
			case LineString lineString -> ctxt.findValueSerializer(LineString.class).serialize(lineString, gen, ctxt);
			case Polygon polygon -> ctxt.findValueSerializer(Polygon.class).serialize(polygon, gen, ctxt);
			case MultiPoint multiPoint -> ctxt.findValueSerializer(MultiPoint.class).serialize(multiPoint, gen, ctxt);
			case MultiLineString multiLineString -> ctxt.findValueSerializer(MultiLineString.class).serialize(multiLineString, gen, ctxt);
			case MultiPolygon multiPolygon -> ctxt.findValueSerializer(MultiPolygon.class).serialize(multiPolygon, gen, ctxt);
			case null -> gen.writeNull();
			default -> throw new IllegalArgumentException("Invalid Geometry type: " + value.getGeometry().getClass().getSimpleName());
		}

		gen.writeName("properties");
		if (value.getProperties() != null) {
			gen.writePOJO(value.getProperties());
		} else {
			gen.writeNull();
		}

		gen.writeEndObject();
	}
}
