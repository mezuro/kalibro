package org.checkstyle;

import static org.junit.Assert.assertSame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.kalibro.core.model.enums.Statistic;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckstyleMetric.class)
public class PreMetricResultTest extends TestCase {

	private CheckstyleMetric metric;
	private NativeMetric nativeMetric;

	private PreMetricResult result;

	@Before
	public void setUp() {
		mockMetric();
		result = new PreMetricResult(metric);
	}

	private void mockMetric() {
		metric = PowerMockito.mock(CheckstyleMetric.class);
		nativeMetric = new NativeMetric("PreMetricResultTest", Granularity.PACKAGE, Language.CPP);
		PowerMockito.when(metric.getNativeMetric()).thenReturn(nativeMetric);
		PowerMockito.when(metric.getAggregationType()).thenReturn(Statistic.AVERAGE);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetNativeMetric() {
		assertSame(nativeMetric, result.getResult().getMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnZeroForNoValue() {
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(0.0, metricResult.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnUniqueValue() {
		result.addValue(42.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(42.0, metricResult.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCalculateStatisticOfAddedValues() {
		result.addValue(42.0);
		result.addValue(58.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(50.0, metricResult.getValue());
	}
}