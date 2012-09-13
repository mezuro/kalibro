package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricResultFixtures.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Statistic;

public class MetricResultTest extends TestCase {

	private MetricResult result;
	private MetricConfiguration configuration;

	@Before
	public void setUp() {
		result = newMetricResult("amloc", 4.2, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0);
		configuration = newConfiguration("amloc").getConfigurationFor(result.getMetric().getName());
	}

	@Test
	public void shouldInitializeWithNativeMetricResult() {
		NativeMetricResult nativeResult = analizoResult("loc");
		result = new MetricResult(nativeResult);
		assertSame(nativeResult.getMetric(), result.getMetric());
		assertSame(nativeResult.getValue(), result.getValue());
	}

	@Test
	public void shouldHaveStatisticsWhenHasDescendentResults() {
		assertTrue(result.hasStatistics());

		result = new MetricResult(analizoResult("nom"));
		assertFalse(result.hasStatistics());
	}

	@Test
	public void shouldGetStatisticFromDescendentResults() {
		for (Statistic statistic : Statistic.values())
			assertEquals(statistic.calculate(result.getDescendentResults()), result.getStatistic(statistic));
	}

	@Test
	public void shouldAddSingleDescendentResult() {
		result.addDescendentResult(0.0);
		assertDeepCollection(result.getDescendentResults(), 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0);
		result.addDescendentResult(14.0);
		assertDeepCollection(result.getDescendentResults(), 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0, 14.0);
	}

	@Test
	public void shouldAddMultipleDescendentResults() {
		result.addDescendentResults(Arrays.asList(0.0, 14.0));
		assertDeepCollection(result.getDescendentResults(), 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0, 14.0);
	}

	@Test
	public void shouldNotChangeValueWhenSettingConfiguration() {
		assertDoubleEquals(4.2, result.getValue());
		result.setConfiguration(configuration);
		assertDoubleEquals(4.2, result.getValue());
	}

	@Test
	public void shouldSetValueFromAggregationFormIfValueIsNaN() {
		for (Statistic statistic : Statistic.values()) {
			result.value = Double.NaN;
			configuration.setAggregationForm(statistic);
			result.setConfiguration(configuration);
			assertDoubleEquals(result.getStatistic(statistic), result.getValue());
		}
	}

	@Test
	public void shouldFindRangeInConfiguration() {
		assertFalse(result.hasRange());

		result.setConfiguration(configuration);
		assertTrue(result.hasRange());
		assertDeepEquals(configuration.getRangeFor(result.getValue()), result.getRange());
	}

	@Test
	public void checkErrorForInexistentRange() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				result.getRange();
			}
		}, "No range found for metric '" + result.getMetric() + "' and value " + result.getValue());
	}

	@Test
	public void shouldGetGradeFromRange() {
		result.setConfiguration(configuration);
		assertDoubleEquals(result.getRange().getGrade(), result.getGrade());
	}

	@Test
	public void shouldGetWeightFromConfiguration() {
		configuration.setWeight(42.0);
		result.setConfiguration(configuration);
		assertDoubleEquals(configuration.getWeight(), result.getWeight());
	}
}