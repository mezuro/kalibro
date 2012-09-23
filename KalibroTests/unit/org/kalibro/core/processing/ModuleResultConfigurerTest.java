package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.newHelloWorldClassResult;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;
import org.kalibro.tests.UnitTest;

public class ModuleResultConfigurerTest extends UnitTest {

	private ModuleResult result;
	private CompoundMetric sc, invalid;
	private Configuration configuration;

	private ModuleResultConfigurer configurer;

	@Before
	public void setUp() {
		result = newHelloWorldClassResult();
		result.removeResultFor(analizoMetric("sc"));
		configuration = newConfiguration("cbo", "lcom4");
		configuration.addMetricConfiguration(createScConfiguration());
		configurer = new ModuleResultConfigurer(result, configuration);
	}

	private MetricConfiguration createScConfiguration() {
		sc = newSc();
		MetricConfiguration scConfiguration = new MetricConfiguration(sc);
		scConfiguration.addRange(new Range());
		return scConfiguration;
	}

	@Test
	public void shouldConfigureNativeMetricResults() {
		configurer.configure();
		assertTrue(result.getResultFor(analizoMetric("cbo")).hasRange());
	}

	@Test
	public void shouldConfigureCompoundMetricResult() {
		configurer.configure();
		assertTrue(result.getResultFor(sc).hasRange());
	}

	@Test
	public void checkGrade() {
		configuration.removeMetric(sc.getName());

		NativeMetric cbo = analizoMetric("cbo");
		MetricResult cboResult = result.getResultFor(cbo);
		MetricConfiguration cboConfiguration = configuration.getConfigurationFor(cbo.getName());

		for (Double weight : new Double[]{0.0, 1.0, 2.0, 3.0, 4.0}) {
			cboConfiguration.setWeight(weight);
			configurer.configure();
			Double numerator = 20.0 + cboResult.getGrade() * weight;
			Double denominator = 2.0 + weight;
			assertDoubleEquals(numerator / denominator, result.getGrade());
		}
	}

	@Test
	public void shouldRemoveCompoundMetricsWhichAreNotInConfiguration() {
		CompoundMetric metric = new CompoundMetric();
		result.addMetricResult(new MetricResult(metric, 42.0));
		configurer.configure();
		assertFalse(result.hasResultFor(metric));
	}

	@Test
	public void shouldComputeCompoundResult() {
		configurer.configure();
		Double cbo = result.getResultFor(analizoMetric("cbo")).getValue();
		Double lcom4 = result.getResultFor(analizoMetric("lcom4")).getValue();
		assertDoubleEquals(cbo * lcom4, result.getResultFor(sc).getValue());
	}

	@Test
	public void shouldIncludeOnlyCompoundMetricsWithCompatibleScope() {
		changeScScope();
		configurer.configure();
		assertFalse(result.hasResultFor(sc));
	}

	private void changeScScope() {
		configuration.removeMetric(sc.getName());
		sc.setScope(Granularity.PACKAGE);
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
	}

	@Test
	public void shouldAddCompoundMetricsWithError() {
		addCompoundMetricWithError();
		configurer.configure();
		assertDeepSet(result.getCompoundMetricsWithError(), invalid);
		assertClassEquals(NullPointerException.class, result.getErrorFor(invalid));
	}

	private void addCompoundMetricWithError() {
		invalid = new CompoundMetric();
		invalid.setName("Invalid");
		invalid.setScript("return cbo > 0 ? 1.0 : null;");
		configuration.addMetricConfiguration(new MetricConfiguration(invalid));
	}
}