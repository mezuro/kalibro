package org.kalibro;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class NativeMetricResultTest extends UnitTest {

	@Test
	public void checkConstruction() {
		NativeMetric metric = mock(NativeMetric.class);
		Double value = mock(Double.class);
		NativeMetricResult result = new NativeMetricResult(metric, value);
		assertSame(metric, result.getMetric());
		assertSame(value, result.getValue());
	}
}