package org.kalibro.core.model;

import static org.analizo.AnalizoStub.*;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class MetricConfigurationFixtures {

	private static Map<String, Double[]> metricThresholds = initializeThresholds();

	private static Map<String, Double[]> initializeThresholds() {
		metricThresholds = new TreeMap<String, Double[]>();
		putThresholds("acc", 2.0, 20.0);
		putThresholds("accm", 1.1, 2.0, 3.1, 4.7);
		putThresholds("amloc", 7.0, 10.0, 13.0, 19.5);
		putThresholds("cbo", 0.8, 1.6, 2.8);
		putThresholds("lcom4", 1.8, 2.8, 4.6);
		putThresholds("loc", 28.0, 70.0, 130.0, 195.0);
		putThresholds("mmloc", 10.0, 13.0, 19.5);
		putThresholds("noa", 2.0, 5.0);
		putThresholds("noc", 1.0, 3.0);
		putThresholds("nom", 4.0, 7.0, 10.0, 15.0);
		putThresholds("total_cof", 0.02, 0.14);
		return metricThresholds;
	}

	private static void putThresholds(String code, Double... thresholds) {
		metricThresholds.put(code, thresholds);
	}

	public static MetricConfiguration configuration(String code) {
		MetricConfiguration configuration = new MetricConfiguration(nativeMetric(code));
		configuration.setCode(code);
		Double[] thresholds = metricThresholds.get(code);
		if (thresholds != null)
			for (Range range : RangeFixtures.createRanges(thresholds))
				configuration.addRange(range);
		return configuration;
	}

	public static Set<String> metricsWithThresholds() {
		return metricThresholds.keySet();
	}

	private MetricConfigurationFixtures() {
		return;
	}
}