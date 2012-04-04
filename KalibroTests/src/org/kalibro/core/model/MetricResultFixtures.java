package org.kalibro.core.model;

import static org.kalibro.core.model.MetricFixtures.*;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public final class MetricResultFixtures {

	private static Map<String, Double> analizoResults;
	private static Map<String, NativeMetricResult> analizoResultsMap;

	static {
		InputStream resource = MetricResultFixtures.class.getResourceAsStream("analizo_results.yml");
		analizoResults = new Yaml().loadAs(resource, Map.class);
		analizoResultsMap = new HashMap<String, NativeMetricResult>();
		for (String code : analizoResults.keySet())
			analizoResultsMap.put(code, newAnalizoResult(code));
	}

	public static NativeMetricResult analizoResult(String code) {
		return analizoResultsMap.get(code);
	}

	public static NativeMetricResult newAnalizoResult(String code) {
		return new NativeMetricResult(newAnalizoMetric(code), analizoResults.get(code));
	}

	public static MetricResult newMetricResult(String code) {
		return new MetricResult(newAnalizoResult(code));
	}

	public static MetricResult newMetricResult(String code, Double value, Double... descendentResults) {
		MetricResult metricResult = new MetricResult(newAnalizoMetric(code), value);
		metricResult.addDescendentResults(Arrays.asList(descendentResults));
		return metricResult;
	}

	private MetricResultFixtures() {
		return;
	}
}