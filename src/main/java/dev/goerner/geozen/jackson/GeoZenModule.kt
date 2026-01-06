package dev.goerner.geozen.jackson

import dev.goerner.geozen.jackson.deserializer.FeatureCollectionDeserializer
import dev.goerner.geozen.jackson.deserializer.FeatureDeserializer
import dev.goerner.geozen.jackson.deserializer.GeometryCollectionDeserializer
import dev.goerner.geozen.jackson.deserializer.GeometryDeserializer
import dev.goerner.geozen.jackson.deserializer.LineStringDeserializer
import dev.goerner.geozen.jackson.deserializer.MultiLineStringDeserializer
import dev.goerner.geozen.jackson.deserializer.MultiPointDeserializer
import dev.goerner.geozen.jackson.deserializer.MultiPolygonDeserializer
import dev.goerner.geozen.jackson.deserializer.PointDeserializer
import dev.goerner.geozen.jackson.deserializer.PolygonDeserializer
import dev.goerner.geozen.jackson.serializer.FeatureCollectionSerializer
import dev.goerner.geozen.jackson.serializer.FeatureSerializer
import dev.goerner.geozen.jackson.serializer.GeometryCollectionSerializer
import dev.goerner.geozen.jackson.serializer.LineStringSerializer
import dev.goerner.geozen.jackson.serializer.MultiLineStringSerializer
import dev.goerner.geozen.jackson.serializer.MultiPointSerializer
import dev.goerner.geozen.jackson.serializer.MultiPolygonSerializer
import dev.goerner.geozen.jackson.serializer.PointSerializer
import dev.goerner.geozen.jackson.serializer.PolygonSerializer
import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.collections.FeatureCollection
import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.databind.module.SimpleModule

class GeoZenModule : SimpleModule("GeoZenModule") {
    init {
        addSerializer<Point>(Point::class.java, PointSerializer())
        addSerializer<LineString>(LineString::class.java, LineStringSerializer())
        addSerializer<Polygon>(Polygon::class.java, PolygonSerializer())
        addSerializer<MultiPoint>(MultiPoint::class.java, MultiPointSerializer())
        addSerializer<MultiLineString>(MultiLineString::class.java, MultiLineStringSerializer())
        addSerializer<MultiPolygon>(MultiPolygon::class.java, MultiPolygonSerializer())
        addSerializer<GeometryCollection>(GeometryCollection::class.java, GeometryCollectionSerializer())
        addSerializer<Feature>(Feature::class.java, FeatureSerializer())
        addSerializer<FeatureCollection>(FeatureCollection::class.java, FeatureCollectionSerializer())

        addDeserializer<Point?>(Point::class.java, PointDeserializer())
        addDeserializer<LineString?>(LineString::class.java, LineStringDeserializer())
        addDeserializer<Polygon?>(Polygon::class.java, PolygonDeserializer())
        addDeserializer<MultiPoint?>(MultiPoint::class.java, MultiPointDeserializer())
        addDeserializer<MultiLineString?>(MultiLineString::class.java, MultiLineStringDeserializer())
        addDeserializer<MultiPolygon?>(MultiPolygon::class.java, MultiPolygonDeserializer())
        addDeserializer<Geometry?>(Geometry::class.java, GeometryDeserializer())
        addDeserializer<GeometryCollection?>(GeometryCollection::class.java, GeometryCollectionDeserializer())
        addDeserializer<Feature?>(Feature::class.java, FeatureDeserializer())
        addDeserializer<FeatureCollection?>(FeatureCollection::class.java, FeatureCollectionDeserializer())
    }
}
