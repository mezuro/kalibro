package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.*;
import org.kalibro.tests.UnitTest;

public class CompoundMetricCalculatorTest extends UnitTest {

	private static final double CBO = new Random().nextDouble();
	private static final double LCOM4 = new Random().nextDouble();
	private static final double SC = CBO * LCOM4;

	private ModuleResult result;
	private NativeMetric cbo, lcom4;
	private CompoundMetric sc, other;
	private Configuration configuration;

	private CompoundMetricCalculator configurer;

	@Before
	public void setUp() {
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		sc = loadFixture("sc", CompoundMetric.class);
		other = new CompoundMetric("Other");
		configuration = loadFixture("sc", Configuration.class);
		configuration.addMetricConfiguration(new MetricConfiguration(other));
		result = new ModuleResult(null, new Module(Granularity.CLASS, "ClassX"));
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(lcom4), LCOM4));
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(cbo), CBO));
		configurer = new CompoundMetricCalculator(result, configuration);
	}

	@Test
	public void shouldComputeCompoundResults() {
		assertFalse(result.hasResultFor(sc));
		configurer.configure();
		assertDoubleEquals(SC, result.getResultFor(sc).getValue());
	}

	@Test
	public void shouldIncludeOnlyCompoundMetricsWithCompatibleScope() {
		sc = (CompoundMetric) configuration.getConfigurationFor(sc).getMetric();
		sc.setScope(Granularity.PACKAGE);
		configurer.configure();
		assertFalse(result.hasResultFor(sc));
	}

	@Test
	public void shouldAddCompoundMetricsWithError() {
		other.setScript("return null;");
		configurer.configure();
		MetricResult otherResult = result.getResultFor(other);
		assertTrue(otherResult.hasError());

		Throwable error = otherResult.getError();
		assertClassEquals(KalibroException.class, error);
		assertEquals("Error evaluating Javascript for: other", error.getMessage());
		assertClassEquals(NullPointerException.class, error.getCause());
	}

	@Test
	public void shouldCalculateCompoundUsingOtherCompound() {
		other.setScript("return 2 * sc();");
		configurer.configure();
		assertDoubleEquals(2 * SC, result.getResultFor(other).getValue());
	}
}