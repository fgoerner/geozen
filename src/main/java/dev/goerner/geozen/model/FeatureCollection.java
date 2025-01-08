package dev.goerner.geozen.model;

import java.util.ArrayList;

/**
 * A {@link FeatureCollection} is a collection of {@link Feature Features}.
 */
public class FeatureCollection {

	private ArrayList<Feature> features;

	/**
	 * Creates a new empty {@link FeatureCollection}.
	 */
	public FeatureCollection() {
		this.features = new ArrayList<>();
	}

	/**
	 * Creates a new {@link FeatureCollection} with the given list of {@link Feature Features}.
	 *
	 * @param features The list of {@link Feature Features} representing the {@link FeatureCollection}.
	 */
	public FeatureCollection(ArrayList<Feature> features) {
		this.features = features;
	}

	public void addFeature(Feature feature) {
		this.features.add(feature);
	}

	public ArrayList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<Feature> features) {
		this.features = features;
	}
}
