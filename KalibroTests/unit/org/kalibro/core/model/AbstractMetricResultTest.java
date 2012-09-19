package org.kalibro.core.model;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Granularity;

public class AbstractMetricResultTest extends TestCase {

	@Test
	public void checkInitialization() {
		MyMetric metric = new MyMetric();
		Double value = Double.NaN;
		MyMetricResult metricResult = new MyMetricResult(metric, value);
		assertSame(metric, metricResult.getMetric());
		assertSame(value, metricResult.getValue());
	}

	private class MyMetricResult extends AbstractMetricResult {

		public MyMetricResult(MyMetric metric, Double value) {
			super(metric, value);
		}
	}

	private class MyMetric extends Metric {

		public MyMetric() {
			super(false, "", Granularity.CLASS);
		}
	}
}