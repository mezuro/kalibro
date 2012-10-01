package org.kalibro;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A metric provided by a base tool.
 * 
 * @author Carlos Morais
 */
public final class NativeMetric extends Metric {

	private SortedSet<Language> languages;

	/**
	 * Creates an instance of NativeMetric with empty description, which can be set after construction.
	 * 
	 * @param name A descriptive name of the metric. Should be unique.
	 * @param scope The granularity of the {@link Module}s to which the metric applies.
	 * @param languages Programming languages to which this metric applies.
	 */
	public NativeMetric(String name, Granularity scope, Language... languages) {
		super(false, name, scope);
		this.languages = new TreeSet<Language>(Arrays.asList(languages));
	}

	public SortedSet<Language> getLanguages() {
		return languages;
	}

	private BaseTool origin;

	@Deprecated
	public BaseTool getOrigin() {
		return origin;
	}

	@Deprecated
	public void setOrigin(BaseTool origin) {
		this.origin = origin;
	}
}