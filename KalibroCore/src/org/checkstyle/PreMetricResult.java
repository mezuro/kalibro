package org.checkstyle;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.NativeMetric;
import org.kalibro.NativeMetricResult;

class PreMetricResult {

	private CheckstyleMetric metric;
	private Collection<Double> values;

	public PreMetricResult(CheckstyleMetric metric) {
		this.metric = metric;
		values = new ArrayList<Double>();
	}

	public void addValue(Double value) {
		values.add(value);
	}

	public NativeMetricResult getResult() {
		NativeMetric nativeMetric = metric.getNativeMetric();
		Double value = values.isEmpty() ? 0.0 : metric.getAggregationType().calculate(values);
		return new NativeMetricResult(nativeMetric, value);
	}
}