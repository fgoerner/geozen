package dev.goerner.geozen.jackson.serializer

import dev.goerner.geozen.model.Feature
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

class FeatureSerializer : ValueSerializer<Feature>() {

    override fun serialize(value: Feature, gen: JsonGenerator, ctxt: SerializationContext) {
        gen.writeStartObject()

        gen.writeStringProperty("type", "Feature")

        if (value.id != null) {
            gen.writeStringProperty("id", value.id)
        }

        gen.writeName("geometry")
        when (val geometry = value.geometry) {
            is Point -> ctxt.findValueSerializer(Point::class.java).serialize(geometry, gen, ctxt)
            is LineString -> ctxt.findValueSerializer(LineString::class.java).serialize(geometry, gen, ctxt)
            is Polygon -> ctxt.findValueSerializer(Polygon::class.java).serialize(geometry, gen, ctxt)
            is MultiPoint -> ctxt.findValueSerializer(MultiPoint::class.java).serialize(geometry, gen, ctxt)
            is MultiLineString -> ctxt.findValueSerializer(MultiLineString::class.java).serialize(geometry, gen, ctxt)
            is MultiPolygon -> ctxt.findValueSerializer(MultiPolygon::class.java).serialize(geometry, gen, ctxt)
            is GeometryCollection -> ctxt.findValueSerializer(GeometryCollection::class.java)
                .serialize(geometry, gen, ctxt)

            null -> gen.writeNull()
            else -> throw IllegalArgumentException(
                "Invalid Geometry type: " + geometry.javaClass.simpleName
            )
        }

        gen.writeName("properties")
        gen.writePOJO(value.properties)

        gen.writeEndObject()
    }
}
