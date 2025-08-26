package dev.goerner.geozen.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.collections.FeatureCollection;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.collections.GeometryCollection;
import dev.goerner.geozen.model.simple_geometry.LineString;
import dev.goerner.geozen.model.multi_geometry.MultiLineString;
import dev.goerner.geozen.model.multi_geometry.MultiPoint;
import dev.goerner.geozen.model.multi_geometry.MultiPolygon;
import dev.goerner.geozen.model.simple_geometry.Point;
import dev.goerner.geozen.model.simple_geometry.Polygon;
import dev.goerner.geozen.jackson.deserializer.FeatureCollectionDeserializer;
import dev.goerner.geozen.jackson.deserializer.FeatureDeserializer;
import dev.goerner.geozen.jackson.deserializer.GeometryCollectionDeserializer;
import dev.goerner.geozen.jackson.deserializer.GeometryDeserializer;
import dev.goerner.geozen.jackson.deserializer.LineStringDeserializer;
import dev.goerner.geozen.jackson.deserializer.MultiLineStringDeserializer;
import dev.goerner.geozen.jackson.deserializer.MultiPointDeserializer;
import dev.goerner.geozen.jackson.deserializer.MultiPolygonDeserializer;
import dev.goerner.geozen.jackson.deserializer.PointDeserializer;
import dev.goerner.geozen.jackson.deserializer.PolygonDeserializer;
import dev.goerner.geozen.jackson.serializer.FeatureCollectionSerializer;
import dev.goerner.geozen.jackson.serializer.FeatureSerializer;
import dev.goerner.geozen.jackson.serializer.GeometryCollectionSerializer;
import dev.goerner.geozen.jackson.serializer.LineStringSerializer;
import dev.goerner.geozen.jackson.serializer.MultiLineStringSerializer;
import dev.goerner.geozen.jackson.serializer.MultiPointSerializer;
import dev.goerner.geozen.jackson.serializer.MultiPolygonSerializer;
import dev.goerner.geozen.jackson.serializer.PointSerializer;
import dev.goerner.geozen.jackson.serializer.PolygonSerializer;

public class GeoZenModule extends SimpleModule {

	public GeoZenModule() {
		super("GeoZenModule");

		addSerializer(Point.class, new PointSerializer());
		addSerializer(LineString.class, new LineStringSerializer());
		addSerializer(Polygon.class, new PolygonSerializer());
		addSerializer(MultiPoint.class, new MultiPointSerializer());
		addSerializer(MultiLineString.class, new MultiLineStringSerializer());
		addSerializer(MultiPolygon.class, new MultiPolygonSerializer());
		addSerializer(GeometryCollection.class, new GeometryCollectionSerializer());
		addSerializer(Feature.class, new FeatureSerializer());
		addSerializer(FeatureCollection.class, new FeatureCollectionSerializer());

		addDeserializer(Point.class, new PointDeserializer());
		addDeserializer(LineString.class, new LineStringDeserializer());
		addDeserializer(Polygon.class, new PolygonDeserializer());
		addDeserializer(MultiPoint.class, new MultiPointDeserializer());
		addDeserializer(MultiLineString.class, new MultiLineStringDeserializer());
		addDeserializer(MultiPolygon.class, new MultiPolygonDeserializer());
		addDeserializer(Geometry.class, new GeometryDeserializer());
		addDeserializer(GeometryCollection.class, new GeometryCollectionDeserializer());
		addDeserializer(Feature.class, new FeatureDeserializer());
		addDeserializer(FeatureCollection.class, new FeatureCollectionDeserializer());
	}
}
