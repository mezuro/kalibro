package org.kalibro.core.model;

import static org.kalibro.core.model.NativeMetricFixtures.*;
import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricResultFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.enums.Granularity.*;

import java.util.Arrays;
import java.util.Date;

import javax.script.ScriptException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.enums.Granularity;

public class ModuleResultTest extends KalibroTestCase {

	private ModuleResult result;
	private CompoundMetric sc, invalid;
	private Configuration configuration;

	@Before
	public void setUp() {
		result = helloWorldClassResult();
		result.metricResults.remove(nativeMetric("sc"));
		configuration = kalibroConfiguration();
		configuration.addMetricConfiguration(createScConfiguration());
		configuration.addMetricConfiguration(createInvalidConfiguration());
	}

	private MetricConfiguration createScConfiguration() {
		sc = CompoundMetricFixtures.sc();
		MetricConfiguration scConfiguration = new MetricConfiguration(sc);
		scConfiguration.addRange(new Range());
		return scConfiguration;
	}

	private MetricConfiguration createInvalidConfiguration() {
		invalid = new CompoundMetric();
		invalid.setName("Invalid");
		invalid.setScript("invalid script");
		return new MetricConfiguration(invalid);
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
	public void shouldConfigureNativeMetricResults() {
		result.setConfiguration(configuration);
		assertTrue(result.getResultFor(nativeMetric("amloc")).hasRange());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldConfigureCompoundMetricResult() {
		result.setConfiguration(configuration);
		assertTrue(result.getResultFor(sc).hasRange());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkGrade() {
		NativeMetric metric = nativeMetric("acc");
		MetricResult metricResult = result.getResultFor(metric);
		MetricConfiguration metricConfiguration = configuration.getConfigurationFor(metric);
		for (Double weight : new Double[]{0.0, 1.0, 2.0, 3.0, 4.0}) {
			metricConfiguration.setWeight(weight);
			result.setConfiguration(configuration);
			Double numerator = 90.0 + metricResult.getGrade() * weight;
			Double denominator = 10.0 + weight;
			assertDoubleEquals(numerator / denominator, result.getGrade());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRemoveCompoundMetricsWhichAreNotInConfiguration() {
		CompoundMetric metric = new CompoundMetric();
		result.addMetricResult(new MetricResult(metric, 42.0));
		result.setConfiguration(configuration);
		assertFalse(result.hasResultFor(metric));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldComputeCompoundResult() {
		result.setConfiguration(configuration);
		Double cbo = result.getResultFor(nativeMetric("cbo")).getValue();
		Double lcom4 = result.getResultFor(nativeMetric("lcom4")).getValue();
		assertDoubleEquals(cbo * lcom4, result.getResultFor(sc).getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldIncludeOnlyCompoundMetricsWithCompatibleScope() {
		changeScScope();
		result.setConfiguration(configuration);
		assertFalse(result.hasResultFor(sc));
	}

	private void changeScScope() {
		configuration.removeMetric(sc);
		sc.setScope(Granularity.PACKAGE);
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMultipleNativeMetricResults() {
		result.addMetricResults(Arrays.asList(nativeMetricResult("sc", 42.0)));
		assertTrue(result.hasResultFor(nativeMetric("sc")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveCompoundMetricsWithError() {
		result.setConfiguration(configuration);
		assertDeepEquals(result.getCompoundMetricsWithError(), invalid);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveCompoundMetricError() {
		result.setConfiguration(configuration);
		assertClassEquals(ScriptException.class, result.getErrorFor(invalid));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByDateThenModule() {
		assertSorted(newResult(0, CLASS, "C"), newResult(0, CLASS, "D"),
			newResult(0, METHOD, "A"), newResult(0, METHOD, "B"),
			newResult(1, APPLICATION, "G"), newResult(1, APPLICATION, "H"),
			newResult(1, PACKAGE, "E"), newResult(1, PACKAGE, "F"));
	}

	private ModuleResult newResult(long date, Granularity granularity, String... name) {
		return new ModuleResult(new Module(granularity, name), new Date(date));
	}
}