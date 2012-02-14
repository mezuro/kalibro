package org.kalibro.core.model;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Granularity;

public class AbstractMetricResultTest extends KalibroTestCase {

	@Test(timeout = UNIT_TIMEOUT)
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
			super("", Granularity.CLASS);
		}

		@Override
		public boolean isCompound() {
			return false;
		}
	}
}