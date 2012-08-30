package org.kalibro.core.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;

/**
 * A metric provided by a base tool.
 * 
 * @author Carlos Morais
 */
public final class NativeMetric extends Metric {

	private String origin;
	private List<Language> languages;

	/**
	 * Creates an instance of NativeMetric with empty description.
	 * 
	 * @param name A descriptive name of the metric. Should be unique.
	 * @param scope The granularity of the {@link Module}s to which the metric applies.
	 * @param languages Programming languages to which this metric applies.
	 */
	public NativeMetric(String name, Granularity scope, Language... languages) {
		this(name, scope, Arrays.asList(languages));
	}

	/**
	 * Creates an instance of NativeMetric with empty description.
	 * 
	 * @param name A descriptive name of the metric. Should be unique.
	 * @param scope The granularity of the {@link Module}s to which the metric applies.
	 * @param languages Programming languages to which this metric applies.
	 */
	public NativeMetric(String name, Granularity scope, Collection<Language> languages) {
		super(false, name, scope, "");
		this.languages = new ArrayList<Language>(languages);
	}

	@Override
	public boolean isCompound() {
		return false;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public List<Language> getLanguages() {
		return languages;
	}
}