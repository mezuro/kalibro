package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;
import static org.kalibro.core.model.RangeFixtures.*;
import static org.kalibro.core.model.RangeLabel.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.Statistic;
import org.kalibro.core.util.Identifier;

public class MetricConfigurationTest extends KalibroTestCase {

	private NativeMetric metric;
	private MetricConfiguration configuration;

	@Before
	public void setUp() {
		metric = analizoMetric("amloc");
		configuration = newMetricConfiguration("amloc");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultAttributes() {
		MetricConfiguration newConfiguration = new MetricConfiguration(metric);
		String expectedCode = Identifier.fromText(metric.getName()).asVariable();
		assertEquals(expectedCode, newConfiguration.getCode());
		assertSame(metric, newConfiguration.getMetric());
		assertDoubleEquals(1.0, newConfiguration.getWeight());
		assertEquals(Statistic.AVERAGE, newConfiguration.getAggregationForm());
		assertTrue(newConfiguration.getRanges().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void configurationsWithDifferentCodeAndMetricShouldNotConflict() {
		MetricConfiguration locConfiguration = metricConfiguration("loc");
		locConfiguration.assertNoConflictWith(configuration);
		configuration.assertNoConflictWith(locConfiguration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void configurationsWithSameCodeShouldConflict() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				MetricConfiguration configurationWithSameCode = new MetricConfiguration(analizoMetric("loc"));
				configurationWithSameCode.setCode(configuration.getCode());
				configurationWithSameCode.assertNoConflictWith(configuration);
			}
		}, "A metric configuration with code 'amloc' already exists");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void configurationsForSameMetricShouldConflict() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				MetricConfiguration configurationForSameMetric = new MetricConfiguration(metric);
				configurationForSameMetric.assertNoConflictWith(configuration);
			}
		}, "There is already a configuration for this metric: " + metric);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasRange() {
		assertTrue(configuration.hasRangeFor(0.0));
		assertTrue(configuration.hasRangeFor(7.0));
		assertTrue(configuration.hasRangeFor(10.0));
		assertTrue(configuration.hasRangeFor(13.0));
		assertTrue(configuration.hasRangeFor(19.5));
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));
		assertFalse(configuration.hasRangeFor(-1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetRange() {
		assertDeepEquals(newRange("amloc", EXCELLENT), configuration.getRangeFor(0.0));
		assertDeepEquals(newRange("amloc", GOOD), configuration.getRangeFor(7.0));
		assertDeepEquals(newRange("amloc", REGULAR), configuration.getRangeFor(10.0));
		assertDeepEquals(newRange("amloc", WARNING), configuration.getRangeFor(13.0));
		assertDeepEquals(newRange("amloc", BAD), configuration.getRangeFor(19.5));
		assertDeepEquals(newRange("amloc", BAD), configuration.getRangeFor(Double.MAX_VALUE));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNoRangeFound() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.getRangeFor(-1.0);
			}
		}, "No range found for value -1.0 and metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddRange() {
		Range newRange = new Range(-1.0, 0.0);
		configuration.addRange(newRange);
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConflictingRange() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.addRange(new Range(6.0, 12.0));
			}
		}, "New range [6.0, 12.0[ would conflict with [0.0, 7.0[");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReplaceExistingRange() {
		Range newRange = new Range(-1.0, 0.0);
		configuration.replaceRange(0.0, newRange);
		assertFalse(configuration.hasRangeFor(0.0));
		assertSame(newRange, configuration.getRangeFor(-1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorReplacingInexistentRange() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.replaceRange(-1.0, new Range(-1.0, 0.0));
			}
		}, "No range found for value -1.0 and metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorForConflictingRangeReplace() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.replaceRange(0.0, new Range());
			}
		}, "New range [-Infinity, Infinity[ would conflict with [7.0, 10.0[");
		assertTrue(configuration.hasRangeFor(0.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveRange() {
		assertEquals(5, configuration.getRanges().size());
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));

		configuration.removeRange(newRange("amloc", BAD));
		assertEquals(4, configuration.getRanges().size());
		assertFalse(configuration.hasRangeFor(Double.MAX_VALUE));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnIfRemovedRangeExisted() {
		assertTrue(configuration.removeRange(newRange("amloc", BAD)));
		assertFalse(configuration.removeRange(new Range(-1.0, 0.0)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByCode() {
		assertSorted(newConfiguration("amloc"), newConfiguration("loc"), newConfiguration("nom"));
	}

	private MetricConfiguration newConfiguration(String metricCode) {
		return new MetricConfiguration(analizoMetric(metricCode));
	}
}