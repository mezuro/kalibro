package org.checkstyle;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.model.enums.Language;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CheckstyleMetric.class)
public class PreMetricResultTest extends KalibroTestCase {

	private String messageKey;
	private NativeMetric nativeMetric;

	private PreMetricResult result;

	@Before
	public void setUp() {
		messageKey = "PreMetricResultTest";
		mockNativeMetric();
		result = new PreMetricResult(messageKey);
	}

	private void mockNativeMetric() {
		nativeMetric = new NativeMetric(messageKey, Granularity.PACKAGE, Language.CPP);
		PowerMockito.mockStatic(CheckstyleMetric.class);
		PowerMockito.when(CheckstyleMetric.getNativeMetricFor(messageKey)).thenReturn(nativeMetric);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetNativeMetric() {
		assertSame(nativeMetric, result.getResult().getMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnUniqueValue() {
		result.addValue(42.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(42.0, metricResult.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCalculateAverageOfAddedValues() {
		result.addValue(42.0);
		result.addValue(58.0);
		NativeMetricResult metricResult = result.getResult();
		assertDoubleEquals(50.0, metricResult.getValue());
	}
}