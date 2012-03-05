package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricResultFixtures.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Statistic;

public class MetricResultTest extends KalibroTestCase {

	private MetricResult result;
	private MetricConfiguration configuration;

	@Before
	public void setUp() {
		result = metricResult("amloc", 4.2, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0);
		configuration = simpleConfiguration().getConfigurationFor(result.getMetric());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInitializeWithNativeMetricResult() {
		NativeMetricResult nativeResult = nativeMetricResult("loc", 42.0);
		result = new MetricResult(nativeResult);
		assertSame(nativeResult.getMetric(), result.getMetric());
		assertSame(nativeResult.getValue(), result.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveStatisticsWhenHasDescendentResults() {
		assertTrue(result.hasStatistics());

		result = new MetricResult(nativeMetricResult("nom", 16.0));
		assertFalse(result.hasStatistics());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetStatisticFromDescendentResults() {
		for (Statistic statistic : Statistic.values())
			assertEquals(statistic.calculate(result.getDescendentResults()), result.getStatistic(statistic));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddSingleDescendentResult() {
		result.addDescendentResult(0.0);
		assertDeepEquals(result.getDescendentResults(), 0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0);
		result.addDescendentResult(14.0);
		assertDeepEquals(result.getDescendentResults(), 0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldAddMultipleDescendentResults() {
		result.addDescendentResults(Arrays.asList(0.0, 14.0));
		assertDeepEquals(result.getDescendentResults(), 0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotChangeValueWhenSettingConfiguration() {
		assertDoubleEquals(4.2, result.getValue());
		result.setConfiguration(configuration);
		assertDoubleEquals(4.2, result.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetValueFromAggregationFormIfValueIsNaN() {
		for (Statistic statistic : Statistic.values()) {
			result.value = Double.NaN;
			configuration.setAggregationForm(statistic);
			result.setConfiguration(configuration);
			assertDoubleEquals(result.getStatistic(statistic), result.getValue());
		}
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldFindRangeInConfiguration() {
		assertFalse(result.hasRange());

		result.setConfiguration(configuration);
		assertTrue(result.hasRange());
		assertDeepEquals(configuration.getRangeFor(result.getValue()), result.getRange());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorForInexistentRange() {
		checkException(new Task() {

			@Override
			public void perform() {
				result.getRange();
			}
		}, IllegalStateException.class,
			"No range found for metric '" + result.getMetric() + "' and value " + result.getValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetGradeFromRange() {
		result.setConfiguration(configuration);
		assertDoubleEquals(result.getRange().getGrade(), result.getGrade());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetWeightFromConfiguration() {
		configuration.setWeight(42.0);
		result.setConfiguration(configuration);
		assertDoubleEquals(configuration.getWeight(), result.getWeight());
	}
}