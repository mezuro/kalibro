package org.kalibro.core.model;

import static org.kalibro.core.model.MetricFixtures.newAnalizoMetric;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class MetricConfigurationFixtures {

	private static Map<String, Double[]> metricThresholds;
	private static Map<String, MetricConfiguration> metricConfigurations;

	static {
		metricThresholds = new HashMap<String, Double[]>();
		metricConfigurations = new HashMap<String, MetricConfiguration>();
		newThresholds("acc", 2.0, 20.0);
		newThresholds("accm", 1.1, 2.0, 3.1, 4.7);
		newThresholds("amloc", 7.0, 10.0, 13.0, 19.5);
		newThresholds("cbo", 0.8, 1.6, 2.8);
		newThresholds("lcom4", 1.8, 2.8, 4.6);
		newThresholds("loc", 28.0, 70.0, 130.0, 195.0);
		newThresholds("mmloc", 10.0, 13.0, 19.5);
		newThresholds("noa", 2.0, 5.0);
		newThresholds("noc", 1.0, 3.0);
		newThresholds("nom", 4.0, 7.0, 10.0, 15.0);
		newThresholds("total_cof", 0.02, 0.14);
	}

	private static void newThresholds(String code, Double... thresholds) {
		metricThresholds.put(code, thresholds);
		metricConfigurations.put(code, newMetricConfiguration(code));
	}

	public static Set<String> configurationCodes() {
		return metricThresholds.keySet();
	}

	public static Double[] thresholds(String code) {
		return metricThresholds.get(code);
	}

	public static MetricConfiguration metricConfiguration(String code) {
		return metricConfigurations.get(code);
	}

	public static MetricConfiguration newMetricConfiguration(String code) {
		MetricConfiguration configuration = new MetricConfiguration(newAnalizoMetric(code));
		configuration.setCode(code);
		Double[] thresholds = metricThresholds.get(code);
		if (thresholds != null)
			for (Range range : RangeFixtures.newRanges(thresholds))
				configuration.addRange(range);
		return configuration;
	}

	private MetricConfigurationFixtures() {
		return;
	}
}