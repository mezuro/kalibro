package org.checkstyle;

import java.util.ArrayList;
import java.util.Collection;

import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.enums.Statistic;

class PreMetricResult {

	private NativeMetric metric;
	private Collection<Double> values;

	public PreMetricResult(String messageKey) {
		this.metric = CheckstyleMetric.getNativeMetricFor(messageKey);
		values = new ArrayList<Double>();
	}

	public void addValue(Double value) {
		values.add(value);
	}

	public NativeMetricResult getResult() {
		return new NativeMetricResult(metric, Statistic.AVERAGE.calculate(values));
	}
}