package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.NativeMetricFixtures.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.Task;

public class ConfigurationTest extends KalibroTestCase {

	private CompoundMetric sc;
	private Configuration configuration;

	@Before
	public void setUp() {
		sc = CompoundMetricFixtures.sc();
		configuration = kalibroConfiguration();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultAttributes() {
		configuration = new Configuration();
		assertEquals("", configuration.getName());
		assertEquals("", configuration.getDescription());
		assertTrue(configuration.getMetricConfigurations().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void toStringShouldBeConfigurationName() {
		assertEquals(configuration.getName(), "" + configuration);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testContains() {
		assertTrue(configuration.contains(nativeMetric("loc")));
		assertTrue(configuration.contains(nativeMetric("nom")));
		assertFalse(configuration.contains(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveCompoundMetrics() {
		assertTrue(configuration.getCompoundMetrics().isEmpty());

		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertDeepEquals(configuration.getCompoundMetrics(), sc);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveNativeMetricsPerOrigin() {
		Map<String, Set<NativeMetric>> nativeMetrics = configuration.getNativeMetrics();
		assertEquals(1, nativeMetrics.size());
		assertEquals(metricsWithThresholds().size(), nativeMetrics.get("Analizo").size());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveConfigurationForMetric() {
		for (String code : metricsWithThresholds())
			assertDeepEquals(configuration(code), configuration.getConfigurationFor(nativeMetric(code)));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoConfigurationFoundForMetricError() {
		final NativeMetric metric = nativeMetric("anpm");
		checkException(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor(metric);
			}
		}, IllegalArgumentException.class, "No configuration found for metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void verifyErrorAddingConflictingMetricConfiguration() {
		checkException(new Task() {

			@Override
			public void perform() {
				configuration.addMetricConfiguration(configuration("acc"));
			}
		}, IllegalArgumentException.class, "A metric configuration with the same code already exists");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveMetric() {
		final NativeMetric metric = nativeMetric("amloc");
		configuration.getConfigurationFor(metric);
		assertTrue(configuration.removeMetric(metric));
		checkException(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor(metric);
			}
		}, IllegalArgumentException.class, "No configuration found for metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateValidCompoundMetric() throws Exception {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		configuration.validateCompoundMetric(sc);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateInvalidCompoundMetric() {
		sc.setScript("return null;");
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.validateCompoundMetric(sc);
			}
		}, NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateNotFoundMetric() {
		checkException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.validateCompoundMetric(new CompoundMetric());
			}
		}, IllegalArgumentException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByName() {
		assertSorted(newConfiguration("A"), newConfiguration("B"), newConfiguration("C"), configuration);
	}

	private Configuration newConfiguration(String name) {
		Configuration newConfiguration = new Configuration();
		newConfiguration.setName(name);
		return newConfiguration;
	}
}