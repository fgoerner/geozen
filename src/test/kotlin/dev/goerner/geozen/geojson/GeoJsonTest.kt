package dev.goerner.geozen.geojson

import dev.goerner.geozen.jackson.GeoZenModule
import dev.goerner.geozen.model.Feature
import dev.goerner.geozen.model.Geometry
import dev.goerner.geozen.model.Position
import dev.goerner.geozen.model.collections.FeatureCollection
import dev.goerner.geozen.model.collections.GeometryCollection
import dev.goerner.geozen.model.multi_geometry.MultiLineString
import dev.goerner.geozen.model.multi_geometry.MultiPoint
import dev.goerner.geozen.model.multi_geometry.MultiPolygon
import dev.goerner.geozen.model.simple_geometry.LineString
import dev.goerner.geozen.model.simple_geometry.Point
import dev.goerner.geozen.model.simple_geometry.Polygon
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper

private lateinit var objectMapper: ObjectMapper

// DTO record for testing nested geometry deserialization
@JvmRecord
data class LocationDTO(
    val name: String?,
    val location: Geometry?,
    val dataSource: String?
)

class GeoJsonTest : FunSpec({

    beforeSpec {
        objectMapper = JsonMapper.builder()
            .addModule(GeoZenModule())
            .build()
    }

    test("nested point deserialization") {
        //given
        val json = """
                {
                    "name": "Some JSON Object",
                    "location": {
                        "type": "Point",
                        "coordinates": [9.123456, 45.987654]
                    },
                    "dataSource": "MANUAL"
                }
                
                """.trimIndent()

        //when
        val locationDTO: LocationDTO = objectMapper.readValue<LocationDTO>(json, LocationDTO::class.java)

        //then
        locationDTO.name shouldBe "Some JSON Object"
        val point = locationDTO.location.shouldBeInstanceOf<Point>()
        point.longitude shouldBe 9.123456
        point.latitude shouldBe 45.987654
        locationDTO.dataSource shouldBe "MANUAL"
    }

    test("point serialization") {
        //given
        val point: Geometry = Point(1.0, 2.0, 3.0)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(point)

        //then
        geoJsonString shouldBe "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}"
    }

    test("line string serialization") {
        //given
        val coordinates: List<Position> = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )
        val lineString: Geometry = LineString(coordinates)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(lineString)

        //then
        geoJsonString shouldBe "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"
    }

    test("polygon serialization") {
        //given
        val coordinates: List<List<Position>> = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0),
                Position(7.0, 8.0, 9.0),
                Position(1.0, 2.0, 3.0)
            ),
            listOf(
                Position(10.0, 11.0, 12.0),
                Position(13.0, 14.0, 15.0),
                Position(16.0, 17.0, 18.0),
                Position(10.0, 11.0, 12.0)
            )
        )
        val polygon: Geometry = Polygon(coordinates)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(polygon)

        //then
        geoJsonString shouldBe "{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}"
    }

    test("multi point serialization") {
        //given
        val coordinates: List<Position> = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )
        val multiPoint: Geometry = MultiPoint(coordinates)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(multiPoint)

        //then
        geoJsonString shouldBe "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"
    }

    test("multi line string serialization") {
        //given
        val coordinates: List<List<Position>> = listOf(
            listOf(
                Position(1.0, 2.0, 3.0),
                Position(4.0, 5.0, 6.0)
            ),
            listOf(
                Position(7.0, 8.0, 9.0),
                Position(10.0, 11.0, 12.0)
            )
        )
        val multiLineString: Geometry = MultiLineString(coordinates)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(multiLineString)

        //then
        geoJsonString shouldBe "{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}"
    }

    test("multi polygon serialization") {
        //given
        val coordinates: List<List<List<Position>>> = listOf(
            listOf(
                listOf(
                    Position(1.0, 2.0, 3.0),
                    Position(4.0, 5.0, 6.0),
                    Position(7.0, 8.0, 9.0),
                    Position(1.0, 2.0, 3.0)
                ),
                listOf(
                    Position(10.0, 11.0, 12.0),
                    Position(13.0, 14.0, 15.0),
                    Position(16.0, 17.0, 18.0),
                    Position(10.0, 11.0, 12.0)
                )
            ),
            listOf(
                listOf(
                    Position(19.0, 20.0, 21.0),
                    Position(22.0, 23.0, 24.0),
                    Position(25.0, 26.0, 27.0),
                    Position(19.0, 20.0, 21.0)
                ),
                listOf(
                    Position(28.0, 29.0, 30.0),
                    Position(31.0, 32.0, 33.0),
                    Position(34.0, 35.0, 36.0),
                    Position(28.0, 29.0, 30.0)
                )
            )
        )
        val multiPolygon: Geometry = MultiPolygon(coordinates)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(multiPolygon)

        //then
        geoJsonString shouldBe "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}"
    }

    test("geometry collection serialization") {
        //given
        val point: Geometry = Point(1.0, 2.0)
        val coordinates: List<Position> = listOf(
            Position(3.0, 4.0),
            Position(5.0, 6.0)
        )
        val lineString: Geometry = LineString(coordinates)
        val geometries: List<Geometry> = listOf(point, lineString)
        val geometryCollection = GeometryCollection(geometries)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(geometryCollection)

        //then
        geoJsonString shouldBe "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}"
    }

    test("feature serialization") {
        //given
        val id = "123"
        val point: Geometry = Point(1.0, 2.0)
        val properties: Map<String, String> = mapOf("name" to "John Doe")
        val feature = Feature(id, point, properties)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(feature)

        //then
        geoJsonString shouldBe "{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}"
    }

    test("feature collection serialization") {
        //given
        val id = "123"
        val point: Geometry = Point(1.0, 2.0)
        val properties: Map<String, String> = mapOf("name" to "John Doe")
        val feature = Feature(id, point, properties)
        val features: List<Feature> = listOf(feature)
        val featureCollection = FeatureCollection(features)

        //when
        val geoJsonString: String? = objectMapper.writeValueAsString(featureCollection)

        //then
        geoJsonString shouldBe "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}"
    }

    test("point deserialization") {
        //given
        val geoJsonString = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}"

        //when
        val geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val point = geometry.shouldBeInstanceOf<Point>()
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        point.altitude shouldBe 3.0
    }

    test("line string deserialization") {
        //given
        val geoJsonString = "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"

        //when
        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val lineString = geometry.shouldBeInstanceOf<LineString>()
        lineString.coordinates.size shouldBe 2
        val position0: Position = lineString.coordinates.first()
        position0.longitude shouldBe 1.0
        position0.latitude shouldBe 2.0
        position0.altitude shouldBe 3.0
        val position1 = lineString.coordinates[1]
        position1.longitude shouldBe 4.0
        position1.latitude shouldBe 5.0
        position1.altitude shouldBe 6.0
    }

    test("polygon deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}"

        //when
        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val polygon = geometry.shouldBeInstanceOf<Polygon>()
        polygon.coordinates.size shouldBe 2
        val exteriorRing: List<Position> = polygon.coordinates.first()
        val exteriorRingPosition0: Position = exteriorRing.first()
        exteriorRingPosition0.longitude shouldBe 1.0
        exteriorRingPosition0.latitude shouldBe 2.0
        exteriorRingPosition0.altitude shouldBe 3.0
        val exteriorRingPosition1 = exteriorRing[1]
        exteriorRingPosition1.longitude shouldBe 4.0
        exteriorRingPosition1.latitude shouldBe 5.0
        exteriorRingPosition1.altitude shouldBe 6.0
        val exteriorRingPosition2 = exteriorRing[2]
        exteriorRingPosition2.longitude shouldBe 7.0
        exteriorRingPosition2.latitude shouldBe 8.0
        exteriorRingPosition2.altitude shouldBe 9.0
        val exteriorRingPosition3 = exteriorRing[3]
        exteriorRingPosition3.longitude shouldBe 1.0
        exteriorRingPosition3.latitude shouldBe 2.0
        exteriorRingPosition3.altitude shouldBe 3.0
        val interiorRing: List<Position> = polygon.coordinates[1]
        val interiorRingPosition0: Position = interiorRing.first()
        interiorRingPosition0.longitude shouldBe 10.0
        interiorRingPosition0.latitude shouldBe 11.0
        interiorRingPosition0.altitude shouldBe 12.0
        val interiorRingPosition1 = interiorRing[1]
        interiorRingPosition1.longitude shouldBe 13.0
        interiorRingPosition1.latitude shouldBe 14.0
        interiorRingPosition1.altitude shouldBe 15.0
        val interiorRingPosition2 = interiorRing[2]
        interiorRingPosition2.longitude shouldBe 16.0
        interiorRingPosition2.latitude shouldBe 17.0
        interiorRingPosition2.altitude shouldBe 18.0
        val interiorRingPosition3 = interiorRing[3]
        interiorRingPosition3.longitude shouldBe 10.0
        interiorRingPosition3.latitude shouldBe 11.0
        interiorRingPosition3.altitude shouldBe 12.0
    }

    test("multi point deserialization") {
        //given
        val geoJsonString = "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"

        //when
        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val multiPoint = geometry.shouldBeInstanceOf<MultiPoint>()
        multiPoint.coordinates.size shouldBe 2
        val position0: Position = multiPoint.coordinates.first()
        position0.longitude shouldBe 1.0
        position0.latitude shouldBe 2.0
        position0.altitude shouldBe 3.0
        val position1 = multiPoint.coordinates[1]
        position1.longitude shouldBe 4.0
        position1.latitude shouldBe 5.0
        position1.altitude shouldBe 6.0
    }

    test("multi line string deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}"

        //when
        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val multiLineString = geometry.shouldBeInstanceOf<MultiLineString>()
        multiLineString.coordinates.size shouldBe 2
        val lineString1: List<Position> = multiLineString.coordinates.first()
        val position0: Position = lineString1.first()
        position0.longitude shouldBe 1.0
        position0.latitude shouldBe 2.0
        position0.altitude shouldBe 3.0
        val position1 = lineString1[1]
        position1.longitude shouldBe 4.0
        position1.latitude shouldBe 5.0
        position1.altitude shouldBe 6.0
        val lineString2: List<Position> = multiLineString.coordinates[1]
        val position2: Position = lineString2.first()
        position2.longitude shouldBe 7.0
        position2.latitude shouldBe 8.0
        position2.altitude shouldBe 9.0
        val position3 = lineString2[1]
        position3.longitude shouldBe 10.0
        position3.latitude shouldBe 11.0
        position3.altitude shouldBe 12.0
    }

    test("multi polygon deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}"

        //when
        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        //then
        val multiPolygon = geometry.shouldBeInstanceOf<MultiPolygon>()
        multiPolygon.coordinates.size shouldBe 2
        val polygon1: List<List<Position>> = multiPolygon.coordinates.first()
        val exteriorRingPosition0: Position = polygon1.first().first()
        exteriorRingPosition0.longitude shouldBe 1.0
        exteriorRingPosition0.latitude shouldBe 2.0
        exteriorRingPosition0.altitude shouldBe 3.0
        val exteriorRingPosition1: Position = polygon1.first()[1]
        exteriorRingPosition1.longitude shouldBe 4.0
        exteriorRingPosition1.latitude shouldBe 5.0
        exteriorRingPosition1.altitude shouldBe 6.0
        val exteriorRingPosition2: Position = polygon1.first()[2]
        exteriorRingPosition2.longitude shouldBe 7.0
        exteriorRingPosition2.latitude shouldBe 8.0
        exteriorRingPosition2.altitude shouldBe 9.0
        val exteriorRingPosition3: Position = polygon1.first()[3]
        exteriorRingPosition3.longitude shouldBe 1.0
        exteriorRingPosition3.latitude shouldBe 2.0
        exteriorRingPosition3.altitude shouldBe 3.0
        val interiorRing1 = polygon1[1]
        val interiorRingPosition0: Position = interiorRing1.first()
        interiorRingPosition0.longitude shouldBe 10.0
        interiorRingPosition0.latitude shouldBe 11.0
        interiorRingPosition0.altitude shouldBe 12.0
        val interiorRingPosition1 = interiorRing1[1]
        interiorRingPosition1.longitude shouldBe 13.0
        interiorRingPosition1.latitude shouldBe 14.0
        interiorRingPosition1.altitude shouldBe 15.0
        val interiorRingPosition2 = interiorRing1[2]
        interiorRingPosition2.longitude shouldBe 16.0
        interiorRingPosition2.latitude shouldBe 17.0
        interiorRingPosition2.altitude shouldBe 18.0
        val interiorRingPosition3 = interiorRing1[3]
        interiorRingPosition3.longitude shouldBe 10.0
        interiorRingPosition3.latitude shouldBe 11.0
        interiorRingPosition3.altitude shouldBe 12.0
        val polygon2: List<List<Position>> = multiPolygon.coordinates[1]
        val exteriorRing2: List<Position> = polygon2.first()
        val exteriorRingPosition4: Position = exteriorRing2.first()
        exteriorRingPosition4.longitude shouldBe 19.0
        exteriorRingPosition4.latitude shouldBe 20.0
        exteriorRingPosition4.altitude shouldBe 21.0
        val exteriorRingPosition5 = exteriorRing2[1]
        exteriorRingPosition5.longitude shouldBe 22.0
        exteriorRingPosition5.latitude shouldBe 23.0
        exteriorRingPosition5.altitude shouldBe 24.0
        val exteriorRingPosition6 = exteriorRing2[2]
        exteriorRingPosition6.longitude shouldBe 25.0
        exteriorRingPosition6.latitude shouldBe 26.0
        exteriorRingPosition6.altitude shouldBe 27.0
        val exteriorRingPosition7 = exteriorRing2[3]
        exteriorRingPosition7.longitude shouldBe 19.0
        exteriorRingPosition7.latitude shouldBe 20.0
        exteriorRingPosition7.altitude shouldBe 21.0
        val interiorRing2 = polygon2[1]
        val interiorRingPosition4: Position = interiorRing2.first()
        interiorRingPosition4.longitude shouldBe 28.0
        interiorRingPosition4.latitude shouldBe 29.0
        interiorRingPosition4.altitude shouldBe 30.0
        val interiorRingPosition5 = interiorRing2[1]
        interiorRingPosition5.longitude shouldBe 31.0
        interiorRingPosition5.latitude shouldBe 32.0
        interiorRingPosition5.altitude shouldBe 33.0
        val interiorRingPosition6 = interiorRing2[2]
        interiorRingPosition6.longitude shouldBe 34.0
        interiorRingPosition6.latitude shouldBe 35.0
        interiorRingPosition6.altitude shouldBe 36.0
        val interiorRingPosition7 = interiorRing2[3]
        interiorRingPosition7.longitude shouldBe 28.0
        interiorRingPosition7.latitude shouldBe 29.0
        interiorRingPosition7.altitude shouldBe 30.0
    }

    test("geometry collection deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}"

        //when
        val geometryCollection: GeometryCollection =
            objectMapper.readValue(geoJsonString, GeometryCollection::class.java)

        //then
        val point: Geometry = geometryCollection.geometries.first()
        val typedPoint = point.shouldBeInstanceOf<Point>()
        typedPoint.longitude shouldBe 1.0
        typedPoint.latitude shouldBe 2.0
        val lineString = geometryCollection.geometries[1]
        val typedLineString = lineString.shouldBeInstanceOf<LineString>()
        typedLineString.coordinates.size shouldBe 2
        val position0: Position = typedLineString.coordinates.first()
        position0.longitude shouldBe 3.0
        position0.latitude shouldBe 4.0
        val position1 = typedLineString.coordinates[1]
        position1.longitude shouldBe 5.0
        position1.latitude shouldBe 6.0
    }

    test("feature deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}"

        //when
        val feature: Feature = objectMapper.readValue(geoJsonString, Feature::class.java)

        //then
        feature.id shouldBe "123"
        val point = feature.geometry.shouldBeInstanceOf<Point>()
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        feature.properties.shouldNotBeNull()
        feature.properties["name"] shouldBe "John Doe"
    }

    test("feature collection deserialization") {
        //given
        val geoJsonString =
            "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}"

        //when
        val featureCollection: FeatureCollection =
            objectMapper.readValue(geoJsonString, FeatureCollection::class.java)

        //then
        featureCollection.features.size shouldBe 1
        val feature: Feature = featureCollection.features.first()
        feature.id shouldBe "123"
        val point = feature.geometry.shouldBeInstanceOf<Point>()
        point.longitude shouldBe 1.0
        point.latitude shouldBe 2.0
        feature.properties.shouldNotBeNull()
        feature.properties["name"] shouldBe "John Doe"
    }

})
