package dev.goerner.geozen.model.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.FeatureCollection;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.GeometryCollection;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.MultiLineString;
import dev.goerner.geozen.model.MultiPoint;
import dev.goerner.geozen.model.MultiPolygon;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Polygon;
import dev.goerner.geozen.model.jackson.deserializer.FeatureCollectionDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.FeatureDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.GeometryCollectionDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.GeometryDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.LineStringDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.MultiLineStringDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.MultiPointDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.MultiPolygonDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.PointDeserializer;
import dev.goerner.geozen.model.jackson.deserializer.PolygonDeserializer;
import dev.goerner.geozen.model.jackson.serializer.FeatureCollectionSerializer;
import dev.goerner.geozen.model.jackson.serializer.FeatureSerializer;
import dev.goerner.geozen.model.jackson.serializer.GeometryCollectionSerializer;
import dev.goerner.geozen.model.jackson.serializer.LineStringSerializer;
import dev.goerner.geozen.model.jackson.serializer.MultiLineStringSerializer;
import dev.goerner.geozen.model.jackson.serializer.MultiPointSerializer;
import dev.goerner.geozen.model.jackson.serializer.MultiPolygonSerializer;
import dev.goerner.geozen.model.jackson.serializer.PointSerializer;
import dev.goerner.geozen.model.jackson.serializer.PolygonSerializer;

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
