package org.checkstyle;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.NativeMetricResult;
import org.kalibro.Statistic;
import org.kalibro.tests.UnitTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckstyleMetric.class)
public class PreMetricResultTest extends UnitTest {

	private CheckstyleMetric metric;

	private PreMetricResult result;

	@Before
	public void setUp() {
		mockMetric();
		result = new PreMetricResult(metric);
	}

	private void mockMetric() {
		metric = PowerMockito.mock(CheckstyleMetric.class);
		PowerMockito.when(metric.getAggregationType()).thenReturn(Statistic.AVERAGE);
	}

	@Test
	public void shouldGetNativeMetric() {
		assertSame(metric, result.getResult().getMetric());
	}

	@Test
	public void shouldReturnZeroForNoValue() {
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(0.0, metricResult.getValue());
	}

	@Test
	public void shouldReturnUniqueValue() {
		result.addValue(42.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(42.0, metricResult.getValue());
	}

	@Test
	public void shouldCalculateStatisticOfAddedValues() {
		result.addValue(42.0);
		result.addValue(58.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(50.0, metricResult.getValue());
	}
}