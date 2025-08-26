package dev.goerner.geozen.model.collections;

import dev.goerner.geozen.model.Feature;

import java.util.List;

/**
 * A {@link FeatureCollection} is a collection of {@link Feature Features}.
 */
public class FeatureCollection {

	private final List<Feature> features;

	/**
	 * Creates a new {@link FeatureCollection} with the given list of {@link Feature Features}.
	 *
	 * @param features The list of {@link Feature Features} representing the {@link FeatureCollection}.
	 */
	public FeatureCollection(List<Feature> features) {
		this.features = List.copyOf(features);
	}

	public List<Feature> getFeatures() {
		return features;
	}
}
