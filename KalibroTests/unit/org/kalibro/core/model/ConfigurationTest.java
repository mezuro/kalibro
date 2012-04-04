package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.*;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

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
		sc = newSc();
		configuration = newConfiguration("cbo", "lcom4");
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
		assertTrue(configuration.contains(analizoMetric("cbo")));
		assertTrue(configuration.contains(analizoMetric("lcom4")));
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
		assertEquals(2, nativeMetrics.get("Analizo").size());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveConfigurationForMetric() {
		assertDeepEquals(metricConfiguration("cbo"), configuration.getConfigurationFor(analizoMetric("cbo")));
		assertDeepEquals(metricConfiguration("lcom4"), configuration.getConfigurationFor(analizoMetric("lcom4")));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoConfigurationFoundForMetricError() {
		final NativeMetric metric = analizoMetric("anpm");
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor(metric);
			}
		}, "No configuration found for metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void verifyErrorAddingConflictingMetricConfiguration() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.addMetricConfiguration(metricConfiguration("cbo"));
			}
		}, "A metric configuration with code 'cbo' already exists");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReplaceExistingMetricConfiguration() {
		MetricConfiguration newMetricConfiguration = metricConfiguration("cbo");
		NativeMetric cbo = analizoMetric("cbo");
		configuration.replaceMetricConfiguration(cbo, newMetricConfiguration);
		assertSame(newMetricConfiguration, configuration.getConfigurationFor(cbo));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorReplacingInexistentMetricConfiguration() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.replaceMetricConfiguration(analizoMetric("noa"), metricConfiguration("noa"));
			}
		}, "No configuration found for metric 'Number of Attributes'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorForConflictingMetricConfigurationReplace() {
		final NativeMetric cbo = analizoMetric("cbo");
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				MetricConfiguration newMetricConfiguration = newMetricConfiguration("cbo");
				newMetricConfiguration.setCode("lcom4");
				configuration.replaceMetricConfiguration(cbo, newMetricConfiguration);
			}
		}, "A metric configuration with code 'lcom4' already exists");
		assertTrue(configuration.contains(cbo));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveMetric() {
		final NativeMetric metric = analizoMetric("cbo");
		configuration.getConfigurationFor(metric);
		assertTrue(configuration.removeMetric(metric));
		assertFalse(configuration.removeMetric(metric));
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor(metric);
			}
		}, "No configuration found for metric '" + metric + "'");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateValidCompoundMetric() {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldInvalidateInvalidCompoundMetric() {
		sc.setScript("return null;");
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.addMetricConfiguration(new MetricConfiguration(sc));
			}
		}, "Compound metric with invalid script: Structural complexity", NullPointerException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSortByName() {
		assertSorted(createConfiguration("A"), createConfiguration("B"), createConfiguration("C"), configuration);
	}

	private Configuration createConfiguration(String name) {
		Configuration newConfiguration = new Configuration();
		newConfiguration.setName(name);
		return newConfiguration;
	}
}