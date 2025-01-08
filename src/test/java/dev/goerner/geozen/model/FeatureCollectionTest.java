package dev.goerner.geozen.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureCollectionTest {

	@Test
	public void testEmptyConstructor() {
		FeatureCollection featureCollection = new FeatureCollection();

		assertEquals(0, featureCollection.getFeatures().size());
	}

	@Test
	public void testFeaturesConstructor() {
		Feature feature = new Feature();
		ArrayList<Feature> features = new ArrayList<>();
		features.add(feature);

		FeatureCollection featureCollection = new FeatureCollection(features);

		assertEquals(1, featureCollection.getFeatures().size());
	}

	@Test
	public void testAddFeature() {
		FeatureCollection featureCollection = new FeatureCollection();
		Feature feature = new Feature();

		featureCollection.addFeature(feature);

		assertEquals(1, featureCollection.getFeatures().size());
	}

	@Test
	public void testSetFeatures() {
		FeatureCollection featureCollection = new FeatureCollection();
		Feature feature = new Feature();
		ArrayList<Feature> features = new ArrayList<>();
		features.add(feature);

		featureCollection.setFeatures(features);

		assertEquals(features, featureCollection.getFeatures());
	}
}
