package org.kalibro.core.model;

import static org.analizo.AnalizoStub.*;

import java.util.Arrays;

public final class MetricResultFixtures {

	public static NativeMetricResult nativeMetricResult(String code, Double value) {
		return new NativeMetricResult(nativeMetric(code), value);
	}

	public static MetricResult metricResult(String code, Double value, Double... descendentResults) {
		MetricResult metricResult = new MetricResult(nativeMetric(code), value);
		metricResult.addDescendentResults(Arrays.asList(descendentResults));
		return metricResult;
	}

	private MetricResultFixtures() {
		return;
	}
}