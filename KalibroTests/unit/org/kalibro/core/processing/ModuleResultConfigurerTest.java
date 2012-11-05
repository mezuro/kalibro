package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ModuleResultConfigurer.class)
public class ModuleResultConfigurerTest extends UnitTest {

	private static final double CBO = new Random().nextDouble();
	private static final double LCOM4 = new Random().nextDouble();
	private static final double SC = CBO * LCOM4;

	private ModuleResult result;
	private NativeMetric cbo, lcom4;
	private CompoundMetric sc, other;
	private Configuration configuration;

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

		NativeMetric totalCof = loadFixture("total_cof", NativeMetric.class);
		result.addMetricResult(new MetricResult(configuration.getConfigurationFor(totalCof), 42.0));
	}

	@Test
	public void shouldComputeCompoundResults() {
		assertFalse(result.hasResultFor(sc));
		configure();
		assertDoubleEquals(SC, result.getResultFor(sc).getValue());
	}

	@Test
	public void shouldIncludeOnlyCompoundMetricsWithCompatibleScope() {
		sc = (CompoundMetric) configuration.getConfigurationFor(sc).getMetric();
		sc.setScope(Granularity.PACKAGE);
		configure();
		assertFalse(result.hasResultFor(sc));
	}

	@Test
	public void shouldAddCompoundMetricsWithError() {
		other.setScript("return null;");
		configure();
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
		configure();
		assertDoubleEquals(2 * SC, result.getResultFor(other).getValue());
	}

	@Test
	public void shouldCalculateGrade() {
		assertDoubleEquals(Double.NaN, result.getGrade());
		configure();
		assertDoubleEquals(10.0, result.getGrade());

		MetricConfiguration cboConfiguration = configuration.getConfigurationFor(cbo);
		cboConfiguration.addRange(rangeThatGradesCboWith(7.0));
		cboConfiguration.setWeight(2.0);
		configure();
		assertDoubleEquals(8.0, result.getGrade());
	}

	private Range rangeThatGradesCboWith(Double grade) {
		Range range = new Range(CBO, CBO + 1.0);
		range.setReading(new Reading("name", grade, Color.WHITE));
		return range;
	}

	@Test
	public void shouldCloseScriptEvaluator() throws Exception {
		JavascriptEvaluator evaluator = spy(new JavascriptEvaluator());
		whenNew(JavascriptEvaluator.class).withNoArguments().thenReturn(evaluator);

		configure();
		verify(evaluator).close();
	}

	private void configure() {
		ModuleResultConfigurer.configure(result, configuration);
	}
}