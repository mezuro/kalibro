package org.checkstyle;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetricResult;
import org.kalibro.core.model.enums.Granularity;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({NativeMetricResult.class, PreModuleResult.class})
public class PreModuleResultTest extends KalibroTestCase {

	private static final String MESSAGE_KEY = "too.many.methods";
	private static final Module MODULE = new Module(Granularity.CLASS, "PreModuleResultTestModule");

	private PreMetricResult preMetricResult;
	private NativeMetricResult metricResult;

	private PreModuleResult result;

	@Before
	public void setUp() throws Exception {
		mockPreMetricResult();
		result = new PreModuleResult(MODULE, CheckstyleStub.nativeMetrics());
	}

	private void mockPreMetricResult() throws Exception {
		metricResult = PowerMockito.mock(NativeMetricResult.class);
		preMetricResult = PowerMockito.mock(PreMetricResult.class);
		PowerMockito.whenNew(PreMetricResult.class).withArguments(any()).thenReturn(preMetricResult);
		PowerMockito.when(preMetricResult.getResult()).thenReturn(metricResult);
		PowerMockito.when(metricResult.getMetric()).thenReturn(CheckstyleMetric.FILE_LENGTH.getNativeMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveModule() {
		Module module = result.getModuleResult().getModule();
		assertEquals(MODULE, module);
		assertEquals(Granularity.CLASS, module.getGranularity());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddValueToPreMetricResult() {
		result.addMetricResult(MESSAGE_KEY, 42.0);
		Mockito.verify(preMetricResult).addValue(42.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void moduleResultShouldContainMetricResults() {
		result.addMetricResult(MESSAGE_KEY, 42.0);
		Collection<NativeMetricResult> metricResults = result.getModuleResult().getMetricResults();
		assertEquals(1, metricResults.size());

		NativeMetricResult firstResult = metricResults.iterator().next();
		assertSame(metricResult, firstResult);
	}
}