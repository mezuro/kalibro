package org.checkstyle;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.kalibro.Granularity;
import org.kalibro.Language;
import org.kalibro.NativeMetric;
import org.kalibro.Statistic;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

/**
 * A metric collected by Checkstyle. Extends {@link NativeMetric} to provide information needed to configure and execute
 * Checkstyle.
 * 
 * @author Carlos Morais
 * @author Eduardo Morais
 */
final class CheckstyleMetric extends NativeMetric {

	private static Set<CheckstyleMetric> metrics;

	static {
		initializeMetrics();
	}

	static Set<NativeMetric> supportedMetrics() {
		return new HashSet<NativeMetric>(metrics);
	}

	static Set<CheckstyleMetric> selectMetrics(Set<NativeMetric> wantedMetrics) {
		Set<CheckstyleMetric> selectedMetrics = new HashSet<CheckstyleMetric>(metrics);
		selectedMetrics.retainAll(wantedMetrics);
		return selectedMetrics;
	}

	private static void initializeMetrics() {
		metrics = new HashSet<CheckstyleMetric>();
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		InputStream metricsStream = CheckstyleMetric.class.getResourceAsStream("supported-metrics.yml");
		for (Object object : yaml.loadAll(metricsStream))
			metrics.add((CheckstyleMetric) object);
	}

	private String moduleName, attributeName, messageKey;
	private boolean treeWalker;
	private Statistic aggregationType;

	private CheckstyleMetric() {
		super("", Granularity.CLASS, Language.JAVA);
		treeWalker = true;
		aggregationType = Statistic.AVERAGE;
	}

	String getMessageKey() {
		return messageKey;
	}

	Statistic getAggregationType() {
		return aggregationType;
	}

	void addToChecker(CheckstyleConfiguration checker) {
		CheckstyleConfiguration parent = treeWalker ? checker.getChildByName("TreeWalker") : checker;
		CheckstyleConfiguration module = parent.getChildByName(moduleName);
		module.addMessageKey(messageKey);
		if (attributeName != null)
			module.addAttribute(attributeName);
	}
}