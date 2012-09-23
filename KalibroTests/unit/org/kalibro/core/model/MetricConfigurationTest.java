package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.analizoMetric;
import static org.kalibro.core.model.RangeFixtures.newRange;
import static org.kalibro.core.model.RangeLabel.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.core.Identifier;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.tests.UnitTest;

public class MetricConfigurationTest extends UnitTest {

	private NativeMetric metric;
	private MetricConfiguration configuration;

	@Before
	public void setUp() {
		metric = analizoMetric("amloc");
		configuration = newMetricConfiguration("amloc");
	}

	@Test
	public void checkDefaultAttributes() {
		MetricConfiguration newConfiguration = new MetricConfiguration(metric);
		String expectedCode = Identifier.fromText(metric.getName()).asVariable();
		assertEquals(expectedCode, newConfiguration.getCode());
		assertSame(metric, newConfiguration.getMetric());
		assertDoubleEquals(1.0, newConfiguration.getWeight());
		assertEquals(Statistic.AVERAGE, newConfiguration.getAggregationForm());
		assertTrue(newConfiguration.getRanges().isEmpty());
	}

	@Test
	public void configurationsWithDifferentCodeAndMetricShouldNotConflict() {
		MetricConfiguration locConfiguration = metricConfiguration("loc");
		locConfiguration.assertNoConflictWith(configuration);
		configuration.assertNoConflictWith(locConfiguration);
	}

	@Test
	public void configurationsWithSameCodeShouldConflict() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				MetricConfiguration configurationWithSameCode = new MetricConfiguration(analizoMetric("loc"));
				configurationWithSameCode.setCode(configuration.getCode());
				configurationWithSameCode.assertNoConflictWith(configuration);
			}
		}).throwsException().withMessage("A metric configuration with code 'amloc' already exists");
	}

	@Test
	public void configurationsForSameMetricShouldConflict() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				MetricConfiguration configurationForSameMetric = new MetricConfiguration(metric);
				configurationForSameMetric.assertNoConflictWith(configuration);
			}
		}).throwsException().withMessage("There is already a configuration for this metric: " + metric);
	}

	@Test
	public void testHasRange() {
		assertTrue(configuration.hasRangeFor(0.0));
		assertTrue(configuration.hasRangeFor(7.0));
		assertTrue(configuration.hasRangeFor(10.0));
		assertTrue(configuration.hasRangeFor(13.0));
		assertTrue(configuration.hasRangeFor(19.5));
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));
		assertFalse(configuration.hasRangeFor(-1.0));
	}

	@Test
	public void testGetRange() {
		assertDeepEquals(newRange("amloc", EXCELLENT), configuration.getRangeFor(0.0));
		assertDeepEquals(newRange("amloc", GOOD), configuration.getRangeFor(7.0));
		assertDeepEquals(newRange("amloc", REGULAR), configuration.getRangeFor(10.0));
		assertDeepEquals(newRange("amloc", WARNING), configuration.getRangeFor(13.0));
		assertDeepEquals(newRange("amloc", BAD), configuration.getRangeFor(19.5));
		assertDeepEquals(newRange("amloc", BAD), configuration.getRangeFor(Double.MAX_VALUE));
	}

	@Test
	public void testNoRangeFound() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.getRangeFor(-1.0);
			}
		}).throwsException().withMessage("No range found for value -1.0 and metric '" + metric + "'");
	}

	@Test
	public void testAddRange() {
		Range newRange = new Range(-1.0, 0.0);
		configuration.addRange(newRange);
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test
	public void testConflictingRange() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.addRange(new Range(6.0, 12.0));
			}
		}).throwsException().withMessage("New range [6.0, 12.0[ would conflict with [0.0, 7.0[");
	}

	@Test
	public void shouldReplaceExistingRange() {
		Range newRange = new Range(-1.0, 0.0);
		configuration.replaceRange(0.0, newRange);
		assertFalse(configuration.hasRangeFor(0.0));
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test
	public void checkErrorReplacingInexistentRange() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.replaceRange(-1.0, new Range(-1.0, 0.0));
			}
		}).throwsException().withMessage("No range found for value -1.0 and metric '" + metric + "'");
	}

	@Test
	public void checkErrorForConflictingRangeReplace() {
		assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.replaceRange(0.0, new Range());
			}
		}).throwsException().withMessage("New range [-Infinity, Infinity[ would conflict with [7.0, 10.0[");
		assertTrue(configuration.hasRangeFor(0.0));
	}

	@Test
	public void testRemoveRange() {
		assertEquals(5, configuration.getRanges().size());
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));

		configuration.removeRange(newRange("amloc", BAD));
		assertEquals(4, configuration.getRanges().size());
		assertFalse(configuration.hasRangeFor(Double.MAX_VALUE));
	}

	@Test
	public void shouldReturnIfRemovedRangeExisted() {
		assertTrue(configuration.removeRange(newRange("amloc", BAD)));
		assertFalse(configuration.removeRange(new Range(-1.0, 0.0)));
	}

	@Test
	public void shouldSortByCode() {
		assertSorted(newConfiguration("amloc"), newConfiguration("loc"), newConfiguration("nom"));
	}

	private MetricConfiguration newConfiguration(String metricCode) {
		return new MetricConfiguration(analizoMetric(metricCode));
	}
}