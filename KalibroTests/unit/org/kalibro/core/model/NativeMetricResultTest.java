package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricFixtures.*;

import org.junit.Test;
import org.kalibro.TestCase;

public class NativeMetricResultTest extends TestCase {

	@Test(timeout = UNIT_TIMEOUT)
	public void checkInitialization() {
		NativeMetric metric = analizoMetric("dit");
		Double value = Math.random();
		NativeMetricResult result = new NativeMetricResult(metric, value);
		assertSame(metric, result.getMetric());
		assertSame(value, result.getValue());
	}
}