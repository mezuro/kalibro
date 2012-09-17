package org.kalibro.core.model;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ConfigurationFixtures.newConfiguration;
import static org.kalibro.core.model.MetricConfigurationFixtures.*;
import static org.kalibro.core.model.MetricFixtures.*;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.concurrent.Task;

public class ConfigurationTest extends TestCase {

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

	@Test
	public void checkDefaultAttributes() {
		configuration = new Configuration();
		assertNull(configuration.getId());
		assertEquals("", configuration.getName());
		assertEquals("", configuration.getDescription());
		assertTrue(configuration.getMetricConfigurations().isEmpty());
	}

	@Test
	public void toStringShouldBeConfigurationName() {
		assertEquals(configuration.getName(), "" + configuration);
	}

	@Test
	public void testContains() {
		assertFalse(configuration.containsMetric("Unknown"));
		assertTrue(configuration.containsMetric(cboName));
		assertTrue(configuration.containsMetric(lcomName));
		assertFalse(configuration.containsMetric(scName));

		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertTrue(configuration.containsMetric(scName));
	}

	@Test
	public void shouldRetrieveCompoundMetrics() {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
		assertDeepSet(configuration.getCompoundMetrics(), sc);

		configuration.removeMetric(scName);
		assertTrue(configuration.getCompoundMetrics().isEmpty());
	}

	@Test
	public void shouldRetrieveNativeMetricsPerOrigin() {
		Map<String, Set<NativeMetric>> nativeMetrics = configuration.getNativeMetrics();
		assertEquals(1, nativeMetrics.size());
		assertEquals(2, nativeMetrics.get("Analizo").size());
	}

	@Test
	public void shouldRetrieveConfigurationForMetric() {
		assertDeepEquals(metricConfiguration("cbo"), configuration.getConfigurationFor(cboName));
		assertDeepEquals(metricConfiguration("lcom4"), configuration.getConfigurationFor(lcomName));
	}

	@Test
	public void checkNoConfigurationFoundForMetricError() {
		assertThat(new Task() {

			@Override
			public void perform() {
				configuration.getConfigurationFor("Unknown");
			}
		}).throwsException().withMessage("No configuration found for metric: Unknown");
	}

	@Test
	public void verifyErrorAddingConflictingMetricConfiguration() {
		assertThat(new Task() {

			@Override
			public void perform() {
				configuration.addMetricConfiguration(metricConfiguration("cbo"));
			}
		}).throwsException().withMessage("A metric configuration with code 'cbo' already exists");
	}

	@Test
	public void shouldReplaceExistingMetricConfiguration() {
		MetricConfiguration newMetricConfiguration = metricConfiguration("cbo");
		configuration.replaceMetricConfiguration(cboName, newMetricConfiguration);
		assertSame(newMetricConfiguration, configuration.getConfigurationFor(cboName));
	}

	@Test
	public void checkErrorReplacingInexistentMetricConfiguration() {
		assertThat(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.replaceMetricConfiguration("Unknown", metricConfiguration("noa"));
			}
		}).throwsException().withMessage("No configuration found for metric: Unknown");
	}

	@Test
	public void checkErrorForConflictingMetricConfigurationReplace() {
		assertThat(new Task() {

			@Override
			public void perform() throws Exception {
				MetricConfiguration newMetricConfiguration = newMetricConfiguration("cbo");
				newMetricConfiguration.setCode("lcom4");
				configuration.replaceMetricConfiguration(cboName, newMetricConfiguration);
			}
		}).throwsException().withMessage("A metric configuration with code 'lcom4' already exists");
		assertTrue(configuration.containsMetric(cboName));
	}

	@Test
	public void testRemoveMetric() {
		configuration.removeMetric(cboName);
		configuration.removeMetric(lcomName);
		assertFalse(configuration.containsMetric(cboName));
		assertFalse(configuration.containsMetric(lcomName));
		assertThat(new Task() {

			@Override
			public void perform() {
				configuration.removeMetric(cboName);
			}
		}).throwsException().withMessage("No configuration found for metric: " + cboName);
	}

	@Test
	public void shouldValidateCompoundMetric() {
		configuration.addMetricConfiguration(new MetricConfiguration(sc));
	}

	@Test
	public void shouldValidateMetricConfiguration() {
		sc.setScript("return null;");
		assertThat(new Task() {

			@Override
			public void perform() throws Exception {
				configuration.addMetricConfiguration(new MetricConfiguration(sc));
			}
		}).throwsException().withCause(NullPointerException.class)
			.withMessage("Metric with invalid code or script: Structural complexity");
	}

	@Test
	public void shouldSortByName() {
		assertSorted(createConfiguration("A"), createConfiguration("B"), createConfiguration("C"), configuration);
	}

	private Configuration createConfiguration(String name) {
		Configuration newConfiguration = new Configuration();
		newConfiguration.setName(name);
		return newConfiguration;
	}
}