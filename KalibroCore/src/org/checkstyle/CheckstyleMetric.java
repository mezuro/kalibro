package org.checkstyle;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	private static Map<String, CheckstyleMetric> metricsMap;

	static {
		initializeMetrics();
	}

	static Set<CheckstyleMetric> supportedMetrics() {
		return metrics;
	}

	static CheckstyleMetric metricFor(String messageKey) {
		return metricsMap.get(messageKey);
	}

	private static void initializeMetrics() {
		metrics = new HashSet<CheckstyleMetric>();
		metricsMap = new HashMap<String, CheckstyleMetric>();
		Yaml yaml = new Yaml();
		yaml.setBeanAccess(BeanAccess.FIELD);
		InputStream metricsStream = CheckstyleMetric.class.getResourceAsStream("supported-metrics.yml");
		for (Object object : yaml.loadAll(metricsStream)) {
			CheckstyleMetric metric = (CheckstyleMetric) object;
			metrics.add(metric);
			metricsMap.put(metric.getMessageKey(), metric);
		}
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