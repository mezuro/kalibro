package org.kalibro;

import static org.junit.Assert.*;
import static org.kalibro.ConfigurationFixtures.newConfiguration;
import static org.kalibro.MetricResultFixtures.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.UnitTest;

public class MetricResultTest extends UnitTest {

	private MetricResult result;
	private MetricConfiguration configuration;

	@Before
	public void setUp() {
		result = newMetricResult("amloc", 4.2, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0);
		configuration = newConfiguration("amloc").getConfigurationFor(result.getMetric());
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
		assertDeepEquals(asList(2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0), result.getDescendentResults());
		result.addDescendentResult(14.0);
		assertDeepEquals(asList(2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0, 14.0), result.getDescendentResults());
	}

	@Test
	public void shouldAddMultipleDescendentResults() {
		result.addDescendentResults(asList(0.0, 14.0));
		assertDeepEquals(asList(2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 0.0, 14.0), result.getDescendentResults());
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
			result.setValue(Double.NaN);
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
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				result.getRange();
			}
		}).throwsException()
			.withMessage("No range found for metric '" + result.getMetric() + "' and value " + result.getValue());
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