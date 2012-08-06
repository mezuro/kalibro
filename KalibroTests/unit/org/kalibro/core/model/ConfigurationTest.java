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

	private String cboName, lcomName, scName;

	@Before
	public void setUp() {
		sc = newSc();
		configuration = newConfiguration("cbo", "lcom4");
		cboName = analizoMetric("cbo").getName();
		lcomName = analizoMetric("lcom4").getName();
		scName = sc.getName();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkDefaultAttributes() {
		configuration = new Configuration();
		assertNull(configuration.getId());
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
		assertFalse(configuration.containsMetric("Unknown"));
		assertTrue(configuration.containsMetric(cboName));
		assertTrue(configuration.containsMetric(lcomName));
		assertFalse(configuration.containsMetric(scName));

		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertTrue(configuration.containsMetric(scName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveCompoundMetrics() {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertDeepEquals(configuration.getCompoundMetrics(), sc);

		configuration.removeMetric(scName);
		assertTrue(configuration.getCompoundMetrics().isEmpty());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveNativeMetricsPerOrigin() {
		Map<String, Set<NativeMetric>> nativeMetrics = configuration.getNativeMetrics();
		assertEquals(1, nativeMetrics.size());
		assertEquals(2, nativeMetrics.get("Analizo").size());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRetrieveConfigurationForMetric() {
		assertDeepEquals(metricConfiguration("cbo"), configuration.getConfigurationFor(cboName));
		assertDeepEquals(metricConfiguration("lcom4"), configuration.getConfigurationFor(lcomName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkNoConfigurationFoundForMetricError() {
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor("Unknown");
			}
		}, "No configuration found for metric: Unknown");
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
		configuration.replaceMetricConfiguration(cboName, newMetricConfiguration);
		assertSame(newMetricConfiguration, configuration.getConfigurationFor(cboName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorReplacingInexistentMetricConfiguration() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.replaceMetricConfiguration("Unknown", metricConfiguration("noa"));
			}
		}, "No configuration found for metric: Unknown");
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkErrorForConflictingMetricConfigurationReplace() {
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				MetricConfiguration newMetricConfiguration = newMetricConfiguration("cbo");
				newMetricConfiguration.setCode("lcom4");
				configuration.replaceMetricConfiguration(cboName, newMetricConfiguration);
			}
		}, "A metric configuration with code 'lcom4' already exists");
		assertTrue(configuration.containsMetric(cboName));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testRemoveMetric() {
		configuration.removeMetric(cboName);
		configuration.removeMetric(lcomName);
		assertFalse(configuration.containsMetric(cboName));
		assertFalse(configuration.containsMetric(lcomName));
		checkKalibroException(new Task() {

			@Override
			public void perform() {
				configuration.removeMetric(cboName);
			}
		}, "No configuration found for metric: " + cboName);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateCompoundMetric() {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidateMetricConfiguration() {
		sc.setScript("return null;");
		checkKalibroException(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.addMetricConfiguration(new MetricConfiguration(sc));
			}
		}, "Metric with invalid code or script: Structural complexity", NullPointerException.class);
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