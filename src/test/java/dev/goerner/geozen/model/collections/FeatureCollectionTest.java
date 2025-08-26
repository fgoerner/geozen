package dev.goerner.geozen.model.collections;

import dev.goerner.geozen.model.Feature;
import dev.goerner.geozen.model.simple_geometry.Point;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FeatureCollectionTest {

	@Test
	public void testFeaturesConstructor() {
		Feature feature = new Feature(new Point(0.0, 0.0));
        List<Feature> features = new ArrayList<>();
		features.add(feature);

		FeatureCollection featureCollection = new FeatureCollection(features);

		assertEquals(1, featureCollection.getFeatures().size());
	}
}
