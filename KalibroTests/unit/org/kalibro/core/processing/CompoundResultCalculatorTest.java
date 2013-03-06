package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(CompoundResultCalculator.class)
public class CompoundResultCalculatorTest extends UnitTest {

	private static final double CBO = new Random().nextDouble();
	private static final double LCOM4 = new Random().nextDouble();
	private static final double SC = CBO * LCOM4;

	private ModuleResult result;
	private NativeMetric cbo, lcom4;
	private CompoundMetric sc, other;
	private Configuration configuration;

	@Before
	public void setUp() {
		mockConfiguration();
		prepareModuleResult();
	}

	private void mockConfiguration() {
		cbo = loadFixture("cbo", NativeMetric.class);
		lcom4 = loadFixture("lcom4", NativeMetric.class);
		sc = loadFixture("sc", CompoundMetric.class);
		other = new CompoundMetric("Other");
		configuration = loadFixture("sc", Configuration.class);
		configuration.addMetricConfiguration(new MetricConfiguration(other));
	}

	private void prepareModuleResult() {
		NativeMetric totalCof = loadFixture("total_cof", NativeMetric.class);
		result = new ModuleResult(null, new Module(Granularity.CLASS, "ClassX"));
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(lcom4), LCOM4));
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(cbo), CBO));
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(totalCof), 42.0));
	}

	@Test
	public void shouldComputeCompoundResults() {
		assertDeepEquals(other, computedResults().get(0).getMetric());
		assertDoubleEquals(SC, computedResults().get(1).getValue());
	}

	@Test
	public void shouldIncludeOnlyCompoundMetricsWithCompatibleScope() {
		sc = (CompoundMetric) configuration.getConfigurationFor(sc).getMetric();
		sc.setScope(Granularity.PACKAGE);
		assertEquals(1, computedResults().size());
		assertDeepEquals(other, computedResults().get(0).getMetric());
	}

	@Test
	public void shouldComputeCompoundMetricsWithError() {
		other.setScript("return null;");
		MetricResult otherResult = computedResults().get(0);
		assertTrue(otherResult.hasError());

		Throwable error = otherResult.getError();
		assertClassEquals(KalibroException.class, error);
		assertEquals("Error evaluating Javascript for: other", error.getMessage());
		assertClassEquals(NullPointerException.class, error.getCause());
	}

	@Test
	public void shouldCalculateCompoundUsingOtherCompound() {
		other.setScript("return 2 * sc();");
		assertDoubleEquals(2 * SC, computedResults().get(0).getValue());
		assertDoubleEquals(SC, computedResults().get(1).getValue());
	}

	private List<MetricResult> computedResults() {
		return new CompoundResultCalculator(result, configuration).calculateCompoundResults();
	}

	@Test
	public void shouldCloseScriptEvaluator() throws Exception {
		JavascriptEvaluator evaluator = spy(new JavascriptEvaluator());
		whenNew(JavascriptEvaluator.class).withNoArguments().thenReturn(evaluator);

		computedResults();
		verify(evaluator).close();
	}
}