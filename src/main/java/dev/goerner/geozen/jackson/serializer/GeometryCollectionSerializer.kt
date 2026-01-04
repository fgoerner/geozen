package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

class GeometryCollectionSerializer : ValueSerializer<GeometryCollection>() {

    override fun serialize(value: GeometryCollection, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "GeometryCollection")

        gen.writeArrayPropertyStart("geometries")
        for (geometry in value.geometries) {
            when (geometry) {
                is Point -> ctxt.findValueSerializer(Point::class.java).serialize(geometry, gen, ctxt)
                is LineString -> ctxt.findValueSerializer(LineString::class.java).serialize(geometry, gen, ctxt)
                is Polygon -> ctxt.findValueSerializer(Polygon::class.java).serialize(geometry, gen, ctxt)
                is MultiPoint -> ctxt.findValueSerializer(MultiPoint::class.java).serialize(geometry, gen, ctxt)
                is MultiLineString -> ctxt.findValueSerializer(MultiLineString::class.java).serialize(geometry, gen, ctxt)
                is MultiPolygon -> ctxt.findValueSerializer(MultiPolygon::class.java).serialize(geometry, gen, ctxt)
                is GeometryCollection -> ctxt.findValueSerializer(GeometryCollection::class.java).serialize(geometry, gen, ctxt)
                else -> throw IllegalArgumentException("Invalid Geometry type: " + geometry.javaClass.getSimpleName())
            }
        }
        gen.writeEndArray()

        gen.writeEndObject()
    }
}
