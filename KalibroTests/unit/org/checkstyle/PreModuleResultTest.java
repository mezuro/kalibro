package org.checkstyle;

import static org.junit.Assert.*;

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

	private static final String MESSAGE_KEY = "PreModuleResultTestMessageKey";
	private static final String MODULE_NAME = "PreModuleResultTestModule";

	private PreMetricResult preMetricResult;
	private NativeMetricResult metricResult;

	private PreModuleResult result;

	@Before
	public void setUp() throws Exception {
		mockPreMetricResult();
		result = new PreModuleResult(MODULE_NAME);
	}

	private void mockPreMetricResult() throws Exception {
		metricResult = PowerMockito.mock(NativeMetricResult.class);
		preMetricResult = PowerMockito.mock(PreMetricResult.class);
		PowerMockito.whenNew(PreMetricResult.class).withArguments(MESSAGE_KEY).thenReturn(preMetricResult);
		PowerMockito.when(preMetricResult.getResult()).thenReturn(metricResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveModule() {
		Module module = result.getModuleResult().getModule();
		assertEquals(MODULE_NAME, module.getName());
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