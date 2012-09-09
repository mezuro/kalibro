package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.kalibroConfiguration;
import static org.kalibro.core.model.MetricFixtures.*;
import static org.kalibro.core.model.MetricResultFixtures.analizoResult;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;
import static org.kalibro.core.model.enums.Granularity.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.core.processing.ModuleResultConfigurer;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModuleResult.class)
public class ModuleResultTest extends TestCase {

	private ModuleResult result;
	private CompoundMetric sc;

	@Before
	public void setUp() {
		result = newHelloWorldClassResult();
		result.removeResultFor(analizoMetric("sc"));
		sc = sc();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testInitialization() {
		Module module = result.getModule();
		Date date = new Date();
		result = new ModuleResult(module, date);
		assertSame(module, result.getModule());
		assertSame(date, result.getDate());
		assertTrue(result.getMetricResults().isEmpty());
		assertTrue(result.getCompoundMetricsWithError().isEmpty());
		assertNull(result.getGrade());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigure() throws Exception {
		Configuration configuration = kalibroConfiguration();
		ModuleResultConfigurer configurer = mock(ModuleResultConfigurer.class);
		whenNew(ModuleResultConfigurer.class).withArguments(result, configuration).thenReturn(configurer);

		result.setConfiguration(configuration);
		Mockito.verify(configurer).configure();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMultipleNativeMetricResults() {
		result.addMetricResults(Arrays.asList(analizoResult("sc")));
		assertTrue(result.hasResultFor(analizoMetric("sc")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveCompoundMetrics() {
		result.addMetricResult(new MetricResult(sc, 42.0));
		result.removeCompoundMetrics();
		assertFalse(result.hasResultFor(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddCompoundMetricWithError() {
		assertTrue(result.getCompoundMetricsWithError().isEmpty());

		Exception error = new Exception();
		result.addCompoundMetricWithError(sc, error);
		assertDeepSet(result.getCompoundMetricsWithError(), sc);
		assertSame(error, result.getErrorFor(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByDateThenModule() {
		assertSorted(newResult(0, CLASS, "C"), newResult(0, CLASS, "D"),
			newResult(0, METHOD, "A"), newResult(0, METHOD, "B"),
			newResult(1, SOFTWARE, "G"), newResult(1, SOFTWARE, "H"),
			newResult(1, PACKAGE, "E"), newResult(1, PACKAGE, "F"));
	}

	private ModuleResult newResult(long date, Granularity granularity, String... name) {
		return new ModuleResult(new Module(granularity, name), new Date(date));
	}
}