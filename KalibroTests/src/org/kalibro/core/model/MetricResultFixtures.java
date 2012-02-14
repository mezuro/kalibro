package org.kalibro.core.model;

import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.Arrays;

public class MetricResultFixtures {

	public static NativeMetricResult nativeMetricResult(String code, Double value) {
		return new NativeMetricResult(nativeMetric(code), value);
	}

	public static MetricResult metricResult(String code, Double value, Double... descendentResults) {
		MetricResult metricResult = new MetricResult(nativeMetric(code), value);
		metricResult.addDescendentResults(Arrays.asList(descendentResults));
		return metricResult;
	}
}