package dev.goerner.geozen.model;

import java.util.Map;

/**
 * A {@link Feature} represents a single spatial object in space. It is defined by an optional {@link #id}, an optional
 * {@link #geometry} and an optional map of {@link #properties}.
 */
public class Feature {

	private String id;
	private Geometry geometry;
	private Map<String, String> properties;

	/**
	 * Creates a new empty {@link Feature}.
	 */
	public Feature() {
		this(null, null, null);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #id}.
	 *
	 * @param id The {@link #id} of the {@link Feature}.
	 */
	public Feature(String id) {
		this(id, null, null);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #geometry}.
	 *
	 * @param geometry The {@link #geometry} of the {@link Feature}.
	 */
	public Feature(Geometry geometry) {
		this(null, geometry, null);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #properties}.
	 *
	 * @param properties The {@link #properties} of the {@link Feature}.
	 */
	public Feature(Map<String, String> properties) {
		this(null, null, properties);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #id} and {@link #properties}.
	 *
	 * @param id         The {@link #id} of the {@link Feature}.
	 * @param properties The {@link #properties} of the {@link Feature}.
	 */
	public Feature(String id, Map<String, String> properties) {
		this(id, null, properties);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #id} and {@link #geometry}.
	 *
	 * @param id       The {@link #id} of the {@link Feature}.
	 * @param geometry The {@link #geometry} of the {@link Feature}.
	 */
	public Feature(String id, Geometry geometry) {
		this(id, geometry, null);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #geometry} and {@link #properties}.
	 *
	 * @param geometry   The {@link #geometry} of the {@link Feature}.
	 * @param properties The {@link #properties} of the {@link Feature}.
	 */
	public Feature(Geometry geometry, Map<String, String> properties) {
		this(null, geometry, properties);
	}

	/**
	 * Creates a new {@link Feature} with the given {@link #id}, {@link #geometry} and {@link #properties}.
	 *
	 * @param id         The {@link #id} of the {@link Feature}.
	 * @param geometry   The {@link #geometry} of the {@link Feature}.
	 * @param properties The {@link #properties} of the {@link Feature}.
	 */
	public Feature(String id, Geometry geometry, Map<String, String> properties) {
		this.id = id;
		this.geometry = geometry;
		this.properties = properties;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
}
