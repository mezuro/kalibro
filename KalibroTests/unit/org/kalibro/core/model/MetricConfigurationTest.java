package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;
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
		metric = nativeMetric("amloc");
		configuration = configuration("amloc");
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
		MetricConfiguration locConfiguration = configuration("loc");
		locConfiguration.assertNoConflictWith(configuration);
		configuration.assertNoConflictWith(locConfiguration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void configurationsWithSameCodeShouldConflict() {
		checkException(new Task() {

			@Override
			public void perform() {
				MetricConfiguration configurationWithSameCode = new MetricConfiguration(nativeMetric("loc"));
				configurationWithSameCode.setCode(configuration.getCode());
				configurationWithSameCode.assertNoConflictWith(configuration);
			}
		}, IllegalArgumentException.class, "A metric configuration with the same code already exists");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void configurationsForSameMetricShouldConflict() {
		checkException(new Task() {

			@Override
			public void perform() {
				MetricConfiguration configurationForSameMetric = new MetricConfiguration(metric);
				configurationForSameMetric.assertNoConflictWith(configuration);
			}
		}, IllegalArgumentException.class, "There is already a configuration for this metric");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testHasRange() {
		assertTrue(configuration.hasRangeFor(0.0));
		assertTrue(configuration.hasRangeFor(7.0));
		assertTrue(configuration.hasRangeFor(10.0));
		assertTrue(configuration.hasRangeFor(13.0));
		assertTrue(configuration.hasRangeFor(19.5));
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));
		assertFalse(configuration.hasRangeFor(- 1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testGetRange() {
		assertDeepEquals(amlocRange(EXCELLENT), configuration.getRangeFor(0.0));
		assertDeepEquals(amlocRange(GOOD), configuration.getRangeFor(7.0));
		assertDeepEquals(amlocRange(REGULAR), configuration.getRangeFor(10.0));
		assertDeepEquals(amlocRange(WARNING), configuration.getRangeFor(13.0));
		assertDeepEquals(amlocRange(BAD), configuration.getRangeFor(19.5));
		assertDeepEquals(amlocRange(BAD), configuration.getRangeFor(Double.MAX_VALUE));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNoRangeFound() {
		checkException(new Task() {

			@Override
			public void perform() {
				configuration.getRangeFor(- 1.0);
			}
		}, IllegalArgumentException.class, "No range found for value -1.0");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testAddRange() {
		Range newRange = new Range(- 1.0, 0.0);
		configuration.addRange(newRange);
		assertSame(newRange, configuration.getRangeFor(- 1.0));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testConflictingRange() {
		checkException(new Task() {

			@Override
			public void perform() {
				configuration.addRange(new Range(6.0, 12.0));
			}
		}, IllegalArgumentException.class, "New range [6.0, 12.0[ would conflict with [0.0, 7.0[");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveRange() {
		assertEquals(5, configuration.getRanges().size());
		assertTrue(configuration.hasRangeFor(Double.MAX_VALUE));

		configuration.removeRange(amlocRange(BAD));
		assertEquals(4, configuration.getRanges().size());
		assertFalse(configuration.hasRangeFor(Double.MAX_VALUE));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnIfRemovedRangeExisted() {
		assertTrue(configuration.removeRange(amlocRange(BAD)));
		assertFalse(configuration.removeRange(new Range(- 1.0, 0.0)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByCode() {
		assertSorted(newConfiguration("amloc"), newConfiguration("loc"), newConfiguration("nom"));
	}

	private MetricConfiguration newConfiguration(String metricCode) {
		return new MetricConfiguration(nativeMetric(metricCode));
	}
}