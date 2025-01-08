package dev.goerner.geozen.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.jackson.GeoZenModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class GeoJsonTest {

	private static ObjectMapper objectMapper;

	@BeforeAll
	static void setup() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new GeoZenModule());
	}

	@Test
	public void testPointSerialization() throws JsonProcessingException {
		Geometry point = new Point(1.0, 2.0, 3.0);

		String geoJsonString = objectMapper.writeValueAsString(point);

		assertEquals("{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}", geoJsonString);
	}

	@Test
	public void testLineStringSerialization() throws JsonProcessingException {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		Geometry lineString = new LineString(coordinates);

		String geoJsonString = objectMapper.writeValueAsString(lineString);

		assertEquals("{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}", geoJsonString);
	}

	@Test
	public void testPolygonSerialization() throws JsonProcessingException {
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		ArrayList<Position> exteriorRing = new ArrayList<>();
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		exteriorRing.add(new Position(4.0, 5.0, 6.0));
		exteriorRing.add(new Position(7.0, 8.0, 9.0));
		exteriorRing.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(exteriorRing);
		ArrayList<Position> interiorRing = new ArrayList<>();
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		interiorRing.add(new Position(13.0, 14.0, 15.0));
		interiorRing.add(new Position(16.0, 17.0, 18.0));
		interiorRing.add(new Position(10.0, 11.0, 12.0));
		coordinates.add(interiorRing);
		Geometry polygon = new Polygon(coordinates);

		String geoJsonString = objectMapper.writeValueAsString(polygon);

		assertEquals("{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}", geoJsonString);
	}

	@Test
	public void testMultiPointSerialization() throws JsonProcessingException {
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(1.0, 2.0, 3.0));
		coordinates.add(new Position(4.0, 5.0, 6.0));
		Geometry multiPoint = new MultiPoint(coordinates);

		String geoJsonString = objectMapper.writeValueAsString(multiPoint);

		assertEquals("{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}", geoJsonString);
	}

	@Test
	public void testMultiLineStringSerialization() throws JsonProcessingException {
		ArrayList<ArrayList<Position>> coordinates = new ArrayList<>();
		ArrayList<Position> lineString1 = new ArrayList<>();
		lineString1.add(new Position(1.0, 2.0, 3.0));
		lineString1.add(new Position(4.0, 5.0, 6.0));
		ArrayList<Position> lineString2 = new ArrayList<>();
		lineString2.add(new Position(7.0, 8.0, 9.0));
		lineString2.add(new Position(10.0, 11.0, 12.0));
		coordinates.add(lineString1);
		coordinates.add(lineString2);
		Geometry multiLineString = new MultiLineString(coordinates);

		String geoJsonString = objectMapper.writeValueAsString(multiLineString);

		assertEquals("{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}", geoJsonString);
	}

	@Test
	public void testMultiPolygonSerialization() throws JsonProcessingException {
		ArrayList<ArrayList<ArrayList<Position>>> coordinates = new ArrayList<>();
		ArrayList<ArrayList<Position>> polygon1 = new ArrayList<>();
		ArrayList<Position> exteriorRing1 = new ArrayList<>();
		exteriorRing1.add(new Position(1.0, 2.0, 3.0));
		exteriorRing1.add(new Position(4.0, 5.0, 6.0));
		exteriorRing1.add(new Position(7.0, 8.0, 9.0));
		exteriorRing1.add(new Position(1.0, 2.0, 3.0));
		polygon1.add(exteriorRing1);
		ArrayList<Position> interiorRing1 = new ArrayList<>();
		interiorRing1.add(new Position(10.0, 11.0, 12.0));
		interiorRing1.add(new Position(13.0, 14.0, 15.0));
		interiorRing1.add(new Position(16.0, 17.0, 18.0));
		interiorRing1.add(new Position(10.0, 11.0, 12.0));
		polygon1.add(interiorRing1);
		coordinates.add(polygon1);
		ArrayList<ArrayList<Position>> polygon2 = new ArrayList<>();
		ArrayList<Position> exteriorRing2 = new ArrayList<>();
		exteriorRing2.add(new Position(19.0, 20.0, 21.0));
		exteriorRing2.add(new Position(22.0, 23.0, 24.0));
		exteriorRing2.add(new Position(25.0, 26.0, 27.0));
		exteriorRing2.add(new Position(19.0, 20.0, 21.0));
		polygon2.add(exteriorRing2);
		ArrayList<Position> interiorRing2 = new ArrayList<>();
		interiorRing2.add(new Position(28.0, 29.0, 30.0));
		interiorRing2.add(new Position(31.0, 32.0, 33.0));
		interiorRing2.add(new Position(34.0, 35.0, 36.0));
		interiorRing2.add(new Position(28.0, 29.0, 30.0));
		polygon2.add(interiorRing2);
		coordinates.add(polygon2);
		Geometry multiPolygon = new MultiPolygon(coordinates);

		String geoJsonString = objectMapper.writeValueAsString(multiPolygon);

		assertEquals("{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}", geoJsonString);
	}

	@Test
	public void testGeometryCollectionSerialization() throws JsonProcessingException {
		Geometry point = new Point(1.0, 2.0);
		ArrayList<Position> coordinates = new ArrayList<>();
		coordinates.add(new Position(3.0, 4.0));
		coordinates.add(new Position(5.0, 6.0));
		Geometry lineString = new LineString(coordinates);
		ArrayList<Geometry> geometries = new ArrayList<>();
		geometries.add(point);
		geometries.add(lineString);
		GeometryCollection geometryCollection = new GeometryCollection(geometries);

		String geoJsonString = objectMapper.writeValueAsString(geometryCollection);

		assertEquals("{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}", geoJsonString);
	}

	@Test
	public void testFeatureSerialization() throws JsonProcessingException {
		String id = "123";
		Geometry point = new Point(1.0, 2.0);
		Map<String, String> properties = new HashMap<>();
		properties.put("name", "John Doe");
		Feature feature = new Feature(id, point, properties);

		String geoJsonString = objectMapper.writeValueAsString(feature);

		assertEquals("{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}", geoJsonString);
	}

	@Test
	public void testFeatureCollectionSerialization() throws JsonProcessingException {
		String id = "123";
		Geometry point = new Point(1.0, 2.0);
		Map<String, String> properties = new HashMap<>();
		properties.put("name", "John Doe");
		Feature feature = new Feature(id, point, properties);
		ArrayList<Feature> features = new ArrayList<>();
		features.add(feature);
		FeatureCollection featureCollection = new FeatureCollection(features);

		String geoJsonString = objectMapper.writeValueAsString(featureCollection);

		assertEquals("{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}", geoJsonString);
	}

	@Test
	void testPointDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"Point\",\"coordinates\":[1.0,2.0,3.0]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(Point.class, geometry);
		Point point = (Point) geometry;
		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals(3.0, point.getAltitude());
	}

	@Test
	void testLineStringDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"LineString\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(LineString.class, geometry);
		LineString lineString = (LineString) geometry;
		assertEquals(2, lineString.getCoordinates().size());
		Position position0 = lineString.getCoordinates().getFirst();
		assertEquals(1.0, position0.getLongitude());
		assertEquals(2.0, position0.getLatitude());
		assertEquals(3.0, position0.getAltitude());
		Position position1 = lineString.getCoordinates().get(1);
		assertEquals(4.0, position1.getLongitude());
		assertEquals(5.0, position1.getLatitude());
		assertEquals(6.0, position1.getAltitude());
	}

	@Test
	void testPolygonDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"Polygon\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(Polygon.class, geometry);
		Polygon polygon = (Polygon) geometry;
		assertEquals(2, polygon.getCoordinates().size());
		ArrayList<Position> exteriorRing = polygon.getCoordinates().getFirst();
		Position exteriorRingPosition0 = exteriorRing.getFirst();
		assertEquals(1.0, exteriorRingPosition0.getLongitude());
		assertEquals(2.0, exteriorRingPosition0.getLatitude());
		assertEquals(3.0, exteriorRingPosition0.getAltitude());
		Position exteriorRingPosition1 = exteriorRing.get(1);
		assertEquals(4.0, exteriorRingPosition1.getLongitude());
		assertEquals(5.0, exteriorRingPosition1.getLatitude());
		assertEquals(6.0, exteriorRingPosition1.getAltitude());
		Position exteriorRingPosition2 = exteriorRing.get(2);
		assertEquals(7.0, exteriorRingPosition2.getLongitude());
		assertEquals(8.0, exteriorRingPosition2.getLatitude());
		assertEquals(9.0, exteriorRingPosition2.getAltitude());
		Position exteriorRingPosition3 = exteriorRing.get(3);
		assertEquals(1.0, exteriorRingPosition3.getLongitude());
		assertEquals(2.0, exteriorRingPosition3.getLatitude());
		assertEquals(3.0, exteriorRingPosition3.getAltitude());
		ArrayList<Position> interiorRing = polygon.getCoordinates().get(1);
		Position interiorRingPosition0 = interiorRing.getFirst();
		assertEquals(10.0, interiorRingPosition0.getLongitude());
		assertEquals(11.0, interiorRingPosition0.getLatitude());
		assertEquals(12.0, interiorRingPosition0.getAltitude());
		Position interiorRingPosition1 = interiorRing.get(1);
		assertEquals(13.0, interiorRingPosition1.getLongitude());
		assertEquals(14.0, interiorRingPosition1.getLatitude());
		assertEquals(15.0, interiorRingPosition1.getAltitude());
		Position interiorRingPosition2 = interiorRing.get(2);
		assertEquals(16.0, interiorRingPosition2.getLongitude());
		assertEquals(17.0, interiorRingPosition2.getLatitude());
		assertEquals(18.0, interiorRingPosition2.getAltitude());
		Position interiorRingPosition3 = interiorRing.get(3);
		assertEquals(10.0, interiorRingPosition3.getLongitude());
		assertEquals(11.0, interiorRingPosition3.getLatitude());
		assertEquals(12.0, interiorRingPosition3.getAltitude());
	}

	@Test
	public void testMultiPointDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"MultiPoint\",\"coordinates\":[[1.0,2.0,3.0],[4.0,5.0,6.0]]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(MultiPoint.class, geometry);
		MultiPoint multiPoint = (MultiPoint) geometry;
		assertEquals(2, multiPoint.getCoordinates().size());
		Position position0 = multiPoint.getCoordinates().getFirst();
		assertEquals(1.0, position0.getLongitude());
		assertEquals(2.0, position0.getLatitude());
		assertEquals(3.0, position0.getAltitude());
		Position position1 = multiPoint.getCoordinates().get(1);
		assertEquals(4.0, position1.getLongitude());
		assertEquals(5.0, position1.getLatitude());
		assertEquals(6.0, position1.getAltitude());
	}

	@Test
	public void testMultiLineStringDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"MultiLineString\",\"coordinates\":[[[1.0,2.0,3.0],[4.0,5.0,6.0]],[[7.0,8.0,9.0],[10.0,11.0,12.0]]]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(MultiLineString.class, geometry);
		MultiLineString multiLineString = (MultiLineString) geometry;
		assertEquals(2, multiLineString.getCoordinates().size());
		ArrayList<Position> lineString1 = multiLineString.getCoordinates().getFirst();
		Position position0 = lineString1.getFirst();
		assertEquals(1.0, position0.getLongitude());
		assertEquals(2.0, position0.getLatitude());
		assertEquals(3.0, position0.getAltitude());
		Position position1 = lineString1.get(1);
		assertEquals(4.0, position1.getLongitude());
		assertEquals(5.0, position1.getLatitude());
		assertEquals(6.0, position1.getAltitude());
		ArrayList<Position> lineString2 = multiLineString.getCoordinates().get(1);
		Position position2 = lineString2.getFirst();
		assertEquals(7.0, position2.getLongitude());
		assertEquals(8.0, position2.getLatitude());
		assertEquals(9.0, position2.getAltitude());
		Position position3 = lineString2.get(1);
		assertEquals(10.0, position3.getLongitude());
		assertEquals(11.0, position3.getLatitude());
		assertEquals(12.0, position3.getAltitude());
	}

	@Test
	public void testMultiPolygonDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"MultiPolygon\",\"coordinates\":[[[[1.0,2.0,3.0],[4.0,5.0,6.0],[7.0,8.0,9.0],[1.0,2.0,3.0]],[[10.0,11.0,12.0],[13.0,14.0,15.0],[16.0,17.0,18.0],[10.0,11.0,12.0]]],[[[19.0,20.0,21.0],[22.0,23.0,24.0],[25.0,26.0,27.0],[19.0,20.0,21.0]],[[28.0,29.0,30.0],[31.0,32.0,33.0],[34.0,35.0,36.0],[28.0,29.0,30.0]]]]}";

		Geometry geometry = objectMapper.readValue(geoJsonString, Geometry.class);

		assertInstanceOf(MultiPolygon.class, geometry);
		MultiPolygon multiPolygon = (MultiPolygon) geometry;
		assertEquals(2, multiPolygon.getCoordinates().size());
		ArrayList<ArrayList<Position>> polygon1 = multiPolygon.getCoordinates().getFirst();
		Position exteriorRingPosition0 = polygon1.getFirst().getFirst();
		assertEquals(1.0, exteriorRingPosition0.getLongitude());
		assertEquals(2.0, exteriorRingPosition0.getLatitude());
		assertEquals(3.0, exteriorRingPosition0.getAltitude());
		Position exteriorRingPosition1 = polygon1.getFirst().get(1);
		assertEquals(4.0, exteriorRingPosition1.getLongitude());
		assertEquals(5.0, exteriorRingPosition1.getLatitude());
		assertEquals(6.0, exteriorRingPosition1.getAltitude());
		Position exteriorRingPosition2 = polygon1.getFirst().get(2);
		assertEquals(7.0, exteriorRingPosition2.getLongitude());
		assertEquals(8.0, exteriorRingPosition2.getLatitude());
		assertEquals(9.0, exteriorRingPosition2.getAltitude());
		Position exteriorRingPosition3 = polygon1.getFirst().get(3);
		assertEquals(1.0, exteriorRingPosition3.getLongitude());
		assertEquals(2.0, exteriorRingPosition3.getLatitude());
		assertEquals(3.0, exteriorRingPosition3.getAltitude());
		ArrayList<Position> interiorRing1 = polygon1.get(1);
		Position interiorRingPosition0 = interiorRing1.getFirst();
		assertEquals(10.0, interiorRingPosition0.getLongitude());
		assertEquals(11.0, interiorRingPosition0.getLatitude());
		assertEquals(12.0, interiorRingPosition0.getAltitude());
		Position interiorRingPosition1 = interiorRing1.get(1);
		assertEquals(13.0, interiorRingPosition1.getLongitude());
		assertEquals(14.0, interiorRingPosition1.getLatitude());
		assertEquals(15.0, interiorRingPosition1.getAltitude());
		Position interiorRingPosition2 = interiorRing1.get(2);
		assertEquals(16.0, interiorRingPosition2.getLongitude());
		assertEquals(17.0, interiorRingPosition2.getLatitude());
		assertEquals(18.0, interiorRingPosition2.getAltitude());
		Position interiorRingPosition3 = interiorRing1.get(3);
		assertEquals(10.0, interiorRingPosition3.getLongitude());
		assertEquals(11.0, interiorRingPosition3.getLatitude());
		assertEquals(12.0, interiorRingPosition3.getAltitude());
		ArrayList<ArrayList<Position>> polygon2 = multiPolygon.getCoordinates().get(1);
		ArrayList<Position> exteriorRing2 = polygon2.getFirst();
		Position exteriorRingPosition4 = exteriorRing2.getFirst();
		assertEquals(19.0, exteriorRingPosition4.getLongitude());
		assertEquals(20.0, exteriorRingPosition4.getLatitude());
		assertEquals(21.0, exteriorRingPosition4.getAltitude());
		Position exteriorRingPosition5 = exteriorRing2.get(1);
		assertEquals(22.0, exteriorRingPosition5.getLongitude());
		assertEquals(23.0, exteriorRingPosition5.getLatitude());
		assertEquals(24.0, exteriorRingPosition5.getAltitude());
		Position exteriorRingPosition6 = exteriorRing2.get(2);
		assertEquals(25.0, exteriorRingPosition6.getLongitude());
		assertEquals(26.0, exteriorRingPosition6.getLatitude());
		assertEquals(27.0, exteriorRingPosition6.getAltitude());
		Position exteriorRingPosition7 = exteriorRing2.get(3);
		assertEquals(19.0, exteriorRingPosition7.getLongitude());
		assertEquals(20.0, exteriorRingPosition7.getLatitude());
		assertEquals(21.0, exteriorRingPosition7.getAltitude());
		ArrayList<Position> interiorRing2 = polygon2.get(1);
		Position interiorRingPosition4 = interiorRing2.getFirst();
		assertEquals(28.0, interiorRingPosition4.getLongitude());
		assertEquals(29.0, interiorRingPosition4.getLatitude());
		assertEquals(30.0, interiorRingPosition4.getAltitude());
		Position interiorRingPosition5 = interiorRing2.get(1);
		assertEquals(31.0, interiorRingPosition5.getLongitude());
		assertEquals(32.0, interiorRingPosition5.getLatitude());
		assertEquals(33.0, interiorRingPosition5.getAltitude());
		Position interiorRingPosition6 = interiorRing2.get(2);
		assertEquals(34.0, interiorRingPosition6.getLongitude());
		assertEquals(35.0, interiorRingPosition6.getLatitude());
		assertEquals(36.0, interiorRingPosition6.getAltitude());
		Position interiorRingPosition7 = interiorRing2.get(3);
		assertEquals(28.0, interiorRingPosition7.getLongitude());
		assertEquals(29.0, interiorRingPosition7.getLatitude());
		assertEquals(30.0, interiorRingPosition7.getAltitude());
	}

	@Test
	public void testGeometryCollectionDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"GeometryCollection\",\"geometries\":[{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},{\"type\":\"LineString\",\"coordinates\":[[3.0,4.0],[5.0,6.0]]}]}";

		GeometryCollection geometryCollection = objectMapper.readValue(geoJsonString, GeometryCollection.class);

		Geometry point = geometryCollection.getGeometries().getFirst();
		assertInstanceOf(Point.class, point);
		Point point1 = (Point) point;
		assertEquals(1.0, point1.getLongitude());
		assertEquals(2.0, point1.getLatitude());
		Geometry lineString = geometryCollection.getGeometries().get(1);
		assertInstanceOf(LineString.class, lineString);
		LineString lineString1 = (LineString) lineString;
		assertEquals(2, lineString1.getCoordinates().size());
		Position position0 = lineString1.getCoordinates().getFirst();
		assertEquals(3.0, position0.getLongitude());
		assertEquals(4.0, position0.getLatitude());
		Position position1 = lineString1.getCoordinates().get(1);
		assertEquals(5.0, position1.getLongitude());
		assertEquals(6.0, position1.getLatitude());
	}

	@Test
	public void testFeatureDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}";

		Feature feature = objectMapper.readValue(geoJsonString, Feature.class);

		assertEquals("123", feature.getId());
		assertInstanceOf(Point.class, feature.getGeometry());
		Point point = (Point) feature.getGeometry();
		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals("John Doe", feature.getProperties().get("name"));
	}

	@Test
	public void testFeatureCollectionDeserialization() throws JsonProcessingException {
		String geoJsonString = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"id\":\"123\",\"geometry\":{\"type\":\"Point\",\"coordinates\":[1.0,2.0]},\"properties\":{\"name\":\"John Doe\"}}]}";

		FeatureCollection featureCollection = objectMapper.readValue(geoJsonString, FeatureCollection.class);

		assertEquals(1, featureCollection.getFeatures().size());
		Feature feature = featureCollection.getFeatures().getFirst();
		assertEquals("123", feature.getId());
		assertInstanceOf(Point.class, feature.getGeometry());
		Point point = (Point) feature.getGeometry();
		assertEquals(1.0, point.getLongitude());
		assertEquals(2.0, point.getLatitude());
		assertEquals("John Doe", feature.getProperties().get("name"));
	}
}
