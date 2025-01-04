package dev.goerner.geozen.model.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Polygon;
import dev.goerner.geozen.model.jackson.deserializer.GeometryDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.LineStringDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.PointDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.PolygonDeserializer;
import dev.goerner.geozen.model.jackson.serializer.LineStringSerializer;
import dev.goerner.geozen.model.jackson.serializer.PointSerializer;
import dev.goerner.geozen.model.jackson.serializer.PolygonSerializer;

public class GeoZenModule extends SimpleModule {

	public GeoZenModule() {
		super("GeoZenModule");

		addSerializer(Point.class, new PointSerializer());
		addSerializer(LineString.class, new LineStringSerializer());
		addSerializer(Polygon.class, new PolygonSerializer());

		addDeserializer(Point.class, new PointDeserializer());
		addDeserializer(LineString.class, new LineStringDeserializer());
		addDeserializer(Polygon.class, new PolygonDeserializer());
		addDeserializer(Geometry.class, new GeometryDeserializer());
	}
}
