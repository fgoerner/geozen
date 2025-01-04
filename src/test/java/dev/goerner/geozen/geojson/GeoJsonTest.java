package dev.goerner.geozen.geojson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.goerner.geozen.model.Geometry;
import dev.goerner.geozen.model.LineString;
import dev.goerner.geozen.model.Point;
import dev.goerner.geozen.model.Polygon;
import dev.goerner.geozen.model.Position;
import dev.goerner.geozen.model.jackson.GeoZenModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
}
