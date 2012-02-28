package org.checkstyle;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;

class PreMetricResult {

	private CheckstyleMetric metric;
	private Collection<Double> values;

	public PreMetricResult(String messageKey) {
		this.metric = CheckstyleMetric.getMetricFor(messageKey);
		values = new ArrayList<Double>();
	}

	public void addValue(Double value) {
		values.add(value);
	}

	public NativeMetricResult getResult() {
		NativeMetric nativeMetric = metric.getNativeMetric();
		Double value = metric.getAggregationType().calculate(values);
		return new NativeMetricResult(nativeMetric, value);
	}
}