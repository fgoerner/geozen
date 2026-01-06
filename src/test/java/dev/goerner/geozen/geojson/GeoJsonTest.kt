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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertInstanceOf
import org.junit.jupiter.api.assertNotNull
import tools.jackson.databind.ObjectMapper
import tools.jackson.databind.json.JsonMapper

class GeoJsonTest {
    @Test
    fun testNestedPointDeserialization() {
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

        val locationDTO: LocationDTO = objectMapper.readValue<LocationDTO>(json, LocationDTO::class.java)

        assertEquals("Some JSON Object", locationDTO.name)
        assertInstanceOf<Point>(locationDTO.location)
        val point = locationDTO.location
        assertEquals(9.123456, point.longitude)
        assertEquals(45.987654, point.latitude)
        assertEquals("MANUAL", locationDTO.dataSource)
    }

    @Test
    fun testPointSerialization() {
        val point: Geometry = Point(1.0, 2.0, 3.0)

        val geoJsonString: String? = objectMapper.writeValueAsString(point)

        assertEquals("{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}", geoJsonString)
    }

    @Test
    fun testLineStringSerialization() {
        val coordinates: List<Position> = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )
        val lineString: Geometry = LineString(coordinates)

        val geoJsonString: String? = objectMapper.writeValueAsString(lineString)

        assertEquals(
            "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}",
            geoJsonString
        )
    }

    @Test
    fun testPolygonSerialization() {
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

        val geoJsonString: String? = objectMapper.writeValueAsString(polygon)

        assertEquals(
            "{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}",
            geoJsonString
        )
    }

    @Test
    fun testMultiPointSerialization() {
        val coordinates: List<Position> = listOf(
            Position(1.0, 2.0, 3.0),
            Position(4.0, 5.0, 6.0)
        )
        val multiPoint: Geometry = MultiPoint(coordinates)

        val geoJsonString: String? = objectMapper.writeValueAsString(multiPoint)

        assertEquals(
            "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}",
            geoJsonString
        )
    }

    @Test
    fun testMultiLineStringSerialization() {
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

        val geoJsonString: String? = objectMapper.writeValueAsString(multiLineString)

        assertEquals(
            "{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}",
            geoJsonString
        )
    }

    @Test
    fun testMultiPolygonSerialization() {
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

        val geoJsonString: String? = objectMapper.writeValueAsString(multiPolygon)

        assertEquals(
            "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}",
            geoJsonString
        )
    }

    @Test
    fun testGeometryCollectionSerialization() {
        val point: Geometry = Point(1.0, 2.0)
        val coordinates: List<Position> = listOf(
            Position(3.0, 4.0),
            Position(5.0, 6.0)
        )
        val lineString: Geometry = LineString(coordinates)
        val geometries: List<Geometry> = listOf(point, lineString)
        val geometryCollection = GeometryCollection(geometries)

        val geoJsonString: String? = objectMapper.writeValueAsString(geometryCollection)

        assertEquals(
            "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}",
            geoJsonString
        )
    }

    @Test
    fun testFeatureSerialization() {
        val id = "123"
        val point: Geometry = Point(1.0, 2.0)
        val properties: Map<String, String> = mapOf("name" to "John Doe")
        val feature = Feature(id, point, properties)

        val geoJsonString: String? = objectMapper.writeValueAsString(feature)

        assertEquals(
            "{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}",
            geoJsonString
        )
    }

    @Test
    fun testFeatureCollectionSerialization() {
        val id = "123"
        val point: Geometry = Point(1.0, 2.0)
        val properties: Map<String, String> = mapOf("name" to "John Doe")
        val feature = Feature(id, point, properties)
        val features: List<Feature> = listOf(feature)
        val featureCollection = FeatureCollection(features)

        val geoJsonString: String? = objectMapper.writeValueAsString(featureCollection)

        assertEquals(
            "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}",
            geoJsonString
        )
    }

    @Test
    fun testPointDeserialization() {
        val geoJsonString = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<Point>(geometry)
        assertEquals(1.0, geometry.longitude)
        assertEquals(2.0, geometry.latitude)
        assertEquals(3.0, geometry.altitude)
    }

    @Test
    fun testLineStringDeserialization() {
        val geoJsonString = "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<LineString>(geometry)
        assertEquals(2, geometry.coordinates.size)
        val position0: Position = geometry.coordinates.first()
        assertEquals(1.0, position0.longitude)
        assertEquals(2.0, position0.latitude)
        assertEquals(3.0, position0.altitude)
        val position1 = geometry.coordinates[1]
        assertEquals(4.0, position1.longitude)
        assertEquals(5.0, position1.latitude)
        assertEquals(6.0, position1.altitude)
    }

    @Test
    fun testPolygonDeserialization() {
        val geoJsonString =
            "{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<Polygon>(geometry)
        assertEquals(2, geometry.coordinates.size)
        val exteriorRing: List<Position> = geometry.coordinates.first()
        val exteriorRingPosition0: Position = exteriorRing.first()
        assertEquals(1.0, exteriorRingPosition0.longitude)
        assertEquals(2.0, exteriorRingPosition0.latitude)
        assertEquals(3.0, exteriorRingPosition0.altitude)
        val exteriorRingPosition1 = exteriorRing[1]
        assertEquals(4.0, exteriorRingPosition1.longitude)
        assertEquals(5.0, exteriorRingPosition1.latitude)
        assertEquals(6.0, exteriorRingPosition1.altitude)
        val exteriorRingPosition2 = exteriorRing[2]
        assertEquals(7.0, exteriorRingPosition2.longitude)
        assertEquals(8.0, exteriorRingPosition2.latitude)
        assertEquals(9.0, exteriorRingPosition2.altitude)
        val exteriorRingPosition3 = exteriorRing[3]
        assertEquals(1.0, exteriorRingPosition3.longitude)
        assertEquals(2.0, exteriorRingPosition3.latitude)
        assertEquals(3.0, exteriorRingPosition3.altitude)
        val interiorRing: List<Position> = geometry.coordinates[1]
        val interiorRingPosition0: Position = interiorRing.first()
        assertEquals(10.0, interiorRingPosition0.longitude)
        assertEquals(11.0, interiorRingPosition0.latitude)
        assertEquals(12.0, interiorRingPosition0.altitude)
        val interiorRingPosition1 = interiorRing[1]
        assertEquals(13.0, interiorRingPosition1.longitude)
        assertEquals(14.0, interiorRingPosition1.latitude)
        assertEquals(15.0, interiorRingPosition1.altitude)
        val interiorRingPosition2 = interiorRing[2]
        assertEquals(16.0, interiorRingPosition2.longitude)
        assertEquals(17.0, interiorRingPosition2.latitude)
        assertEquals(18.0, interiorRingPosition2.altitude)
        val interiorRingPosition3 = interiorRing[3]
        assertEquals(10.0, interiorRingPosition3.longitude)
        assertEquals(11.0, interiorRingPosition3.latitude)
        assertEquals(12.0, interiorRingPosition3.altitude)
    }

    @Test
    fun testMultiPointDeserialization() {
        val geoJsonString = "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<MultiPoint>(geometry)
        assertEquals(2, geometry.coordinates.size)
        val position0: Position = geometry.coordinates.first()
        assertEquals(1.0, position0.longitude)
        assertEquals(2.0, position0.latitude)
        assertEquals(3.0, position0.altitude)
        val position1 = geometry.coordinates[1]
        assertEquals(4.0, position1.longitude)
        assertEquals(5.0, position1.latitude)
        assertEquals(6.0, position1.altitude)
    }

    @Test
    fun testMultiLineStringDeserialization() {
        val geoJsonString =
            "{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<MultiLineString>(geometry)
        assertEquals(2, geometry.coordinates.size)
        val lineString1: List<Position> = geometry.coordinates.first()
        val position0: Position = lineString1.first()
        assertEquals(1.0, position0.longitude)
        assertEquals(2.0, position0.latitude)
        assertEquals(3.0, position0.altitude)
        val position1 = lineString1[1]
        assertEquals(4.0, position1.longitude)
        assertEquals(5.0, position1.latitude)
        assertEquals(6.0, position1.altitude)
        val lineString2: List<Position> = geometry.coordinates[1]
        val position2: Position = lineString2.first()
        assertEquals(7.0, position2.longitude)
        assertEquals(8.0, position2.latitude)
        assertEquals(9.0, position2.altitude)
        val position3 = lineString2[1]
        assertEquals(10.0, position3.longitude)
        assertEquals(11.0, position3.latitude)
        assertEquals(12.0, position3.altitude)
    }

    @Test
    fun testMultiPolygonDeserialization() {
        val geoJsonString =
            "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}"

        val geometry: Geometry = objectMapper.readValue(geoJsonString, Geometry::class.java)

        assertInstanceOf<MultiPolygon>(geometry)
        assertEquals(2, geometry.coordinates.size)
        val polygon1: List<List<Position>> = geometry.coordinates.first()
        val exteriorRingPosition0: Position = polygon1.first().first()
        assertEquals(1.0, exteriorRingPosition0.longitude)
        assertEquals(2.0, exteriorRingPosition0.latitude)
        assertEquals(3.0, exteriorRingPosition0.altitude)
        val exteriorRingPosition1: Position = polygon1.first()[1]
        assertEquals(4.0, exteriorRingPosition1.longitude)
        assertEquals(5.0, exteriorRingPosition1.latitude)
        assertEquals(6.0, exteriorRingPosition1.altitude)
        val exteriorRingPosition2: Position = polygon1.first()[2]
        assertEquals(7.0, exteriorRingPosition2.longitude)
        assertEquals(8.0, exteriorRingPosition2.latitude)
        assertEquals(9.0, exteriorRingPosition2.altitude)
        val exteriorRingPosition3: Position = polygon1.first()[3]
        assertEquals(1.0, exteriorRingPosition3.longitude)
        assertEquals(2.0, exteriorRingPosition3.latitude)
        assertEquals(3.0, exteriorRingPosition3.altitude)
        val interiorRing1 = polygon1[1]
        val interiorRingPosition0: Position = interiorRing1.first()
        assertEquals(10.0, interiorRingPosition0.longitude)
        assertEquals(11.0, interiorRingPosition0.latitude)
        assertEquals(12.0, interiorRingPosition0.altitude)
        val interiorRingPosition1 = interiorRing1[1]
        assertEquals(13.0, interiorRingPosition1.longitude)
        assertEquals(14.0, interiorRingPosition1.latitude)
        assertEquals(15.0, interiorRingPosition1.altitude)
        val interiorRingPosition2 = interiorRing1[2]
        assertEquals(16.0, interiorRingPosition2.longitude)
        assertEquals(17.0, interiorRingPosition2.latitude)
        assertEquals(18.0, interiorRingPosition2.altitude)
        val interiorRingPosition3 = interiorRing1[3]
        assertEquals(10.0, interiorRingPosition3.longitude)
        assertEquals(11.0, interiorRingPosition3.latitude)
        assertEquals(12.0, interiorRingPosition3.altitude)
        val polygon2: List<List<Position>> = geometry.coordinates[1]
        val exteriorRing2: List<Position> = polygon2.first()
        val exteriorRingPosition4: Position = exteriorRing2.first()
        assertEquals(19.0, exteriorRingPosition4.longitude)
        assertEquals(20.0, exteriorRingPosition4.latitude)
        assertEquals(21.0, exteriorRingPosition4.altitude)
        val exteriorRingPosition5 = exteriorRing2[1]
        assertEquals(22.0, exteriorRingPosition5.longitude)
        assertEquals(23.0, exteriorRingPosition5.latitude)
        assertEquals(24.0, exteriorRingPosition5.altitude)
        val exteriorRingPosition6 = exteriorRing2[2]
        assertEquals(25.0, exteriorRingPosition6.longitude)
        assertEquals(26.0, exteriorRingPosition6.latitude)
        assertEquals(27.0, exteriorRingPosition6.altitude)
        val exteriorRingPosition7 = exteriorRing2[3]
        assertEquals(19.0, exteriorRingPosition7.longitude)
        assertEquals(20.0, exteriorRingPosition7.latitude)
        assertEquals(21.0, exteriorRingPosition7.altitude)
        val interiorRing2 = polygon2[1]
        val interiorRingPosition4: Position = interiorRing2.first()
        assertEquals(28.0, interiorRingPosition4.longitude)
        assertEquals(29.0, interiorRingPosition4.latitude)
        assertEquals(30.0, interiorRingPosition4.altitude)
        val interiorRingPosition5 = interiorRing2[1]
        assertEquals(31.0, interiorRingPosition5.longitude)
        assertEquals(32.0, interiorRingPosition5.latitude)
        assertEquals(33.0, interiorRingPosition5.altitude)
        val interiorRingPosition6 = interiorRing2[2]
        assertEquals(34.0, interiorRingPosition6.longitude)
        assertEquals(35.0, interiorRingPosition6.latitude)
        assertEquals(36.0, interiorRingPosition6.altitude)
        val interiorRingPosition7 = interiorRing2[3]
        assertEquals(28.0, interiorRingPosition7.longitude)
        assertEquals(29.0, interiorRingPosition7.latitude)
        assertEquals(30.0, interiorRingPosition7.altitude)
    }

    @Test
    fun testGeometryCollectionDeserialization() {
        val geoJsonString =
            "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}"

        val geometryCollection: GeometryCollection =
            objectMapper.readValue(geoJsonString, GeometryCollection::class.java)

        val point: Geometry = geometryCollection.geometries.first()
        assertInstanceOf<Point>(point)
        assertEquals(1.0, point.longitude)
        assertEquals(2.0, point.latitude)
        val lineString = geometryCollection.geometries[1]
        assertInstanceOf<LineString>(lineString)
        assertEquals(2, lineString.coordinates.size)
        val position0: Position = lineString.coordinates.first()
        assertEquals(3.0, position0.longitude)
        assertEquals(4.0, position0.latitude)
        val position1 = lineString.coordinates[1]
        assertEquals(5.0, position1.longitude)
        assertEquals(6.0, position1.latitude)
    }

    @Test
    fun testFeatureDeserialization() {
        val geoJsonString =
            "{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}"

        val feature: Feature = objectMapper.readValue(geoJsonString, Feature::class.java)

        assertEquals("123", feature.id)
        assertInstanceOf<Point>(feature.geometry)
        val point = feature.geometry
        assertEquals(1.0, point.longitude)
        assertEquals(2.0, point.latitude)
        assertNotNull(feature.properties)
        assertEquals("John Doe", feature.properties["name"])
    }

    @Test
    fun testFeatureCollectionDeserialization() {
        val geoJsonString =
            "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}"

        val featureCollection: FeatureCollection =
            objectMapper.readValue(geoJsonString, FeatureCollection::class.java)

        assertEquals(1, featureCollection.features.size)
        val feature: Feature = featureCollection.features.first()
        assertEquals("123", feature.id)
        assertInstanceOf<Point>(feature.geometry)
        val point = feature.geometry
        assertEquals(1.0, point.longitude)
        assertEquals(2.0, point.latitude)
        assertNotNull(feature.properties)
        assertEquals("John Doe", feature.properties["name"])
    }

    // DTO record for testing nested geometry deserialization
    @JvmRecord
    data class LocationDTO(
        val name: String?,
        val location: Geometry?,
        val dataSource: String?
    )

    companion object {
        private lateinit var objectMapper: ObjectMapper

        @JvmStatic
        @BeforeAll
        fun setup() {
            objectMapper = JsonMapper.builder()
                .addModule(GeoZenModule())
                .build()
        }
    }
}
