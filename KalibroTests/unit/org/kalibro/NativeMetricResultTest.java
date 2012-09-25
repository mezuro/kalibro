package org.kalibro;

import static org.junit.Assert.assertSame;
import static org.kalibro.core.model.MetricFixtures.analizoMetric;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricResultTest extends UnitTest {

	@Test
	public void checkInitialization() {
		NativeMetric metric = analizoMetric("dit");
		Double value = Math.random();
		NativeMetricResult result = new NativeMetricResult(metric, value);
		assertSame(metric, result.getMetric());
		assertSame(value, result.getValue());
	}
}