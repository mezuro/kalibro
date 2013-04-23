package org.kalibro;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UnitTest;

public class AbstractMetricResultTest extends UnitTest {

	private Metric metric;

	@Before
	public void setUp() {
		metric = mock(Metric.class);
	}

	@Test
	public void shouldSortByMetric() {
		Metric second = mock(Metric.class);
		when(metric.compareTo(second)).thenReturn(-1);
		when(second.compareTo(metric)).thenReturn(1);
		assertSorted(result(1.0), result(second, 0.0));
	}

	@Test
	public void shouldIdentifyByMetric() {
		assertEquals(result(42.0), result(28.0));
	}

	@Test
	public void checkConstruction() {
		Double value = mock(Double.class);
		AbstractMetricResult metricResult = result(value);
		assertSame(metric, metricResult.getMetric());
		assertSame(value, metricResult.getValue());
	}

	private AbstractMetricResult result(Double value) {
		return result(metric, value);
	}

	private AbstractMetricResult result(Metric theMetric, Double value) {
		return new AbstractMetricResult(theMetric, value)
			{ /* just for test */ };
	}
}