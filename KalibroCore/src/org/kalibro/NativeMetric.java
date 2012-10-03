package org.kalibro;

import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Metric provided by a base tool.
 * 
 * @author Carlos Morais
 */
public class NativeMetric extends Metric {

	private SortedSet<Language> languages;

	@SuppressWarnings("unused" /* used by SnakeYaml */)
	private NativeMetric() {
		this("", Granularity.CLASS);
	}

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

	public final SortedSet<Language> getLanguages() {
		return languages;
	}
}