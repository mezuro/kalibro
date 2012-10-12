package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

public class ConfigurationAcceptanceTest extends AcceptanceTest {

	private File file;
	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc", Configuration.class);
		file = new File(Environment.dotKalibro(), "Configuration-exported.yml");
		file.deleteOnExit();
	}

	@After
	public void tearDown() {
		for (Configuration each : Configuration.all())
			each.delete();
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		assertNotSaved();

		configuration.save();
		assertSaved();

		configuration.setDescription("Another description");
		assertFalse(Configuration.all().first().deepEquals(configuration));

		configuration.save();
		assertSaved();

		configuration.delete();
		assertNotSaved();
	}

	private void assertNotSaved() {
		assertTrue(Configuration.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(asSet(configuration), Configuration.all());
	}

	@Theory
	public void nameShouldBeRequiredAndUnique(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		configuration.setName(" ");
		assertThat(save()).throwsException().withMessage("Configuration requires name.");

		configuration.setName("Unique");
		configuration.save();

		configuration = new Configuration("Unique");
		assertThat(save()).doThrow(RollbackException.class);
	}

	private VoidTask save() {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		};
	}

	@Test
	public void shouldValidateMetricConfigurations() {
		metricsInSameConfigurationShouldNotHaveDuplicateCodes();
		metricsInSameConfigurationShouldNotHaveDuplicateMetrics();
		shouldValidateScripts();
	}

	private void metricsInSameConfigurationShouldNotHaveDuplicateCodes() {
		String code = configuration.getMetricConfigurations().first().getCode();
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		metricConfiguration.setCode(code);
		assertThat(addMetricConfiguration(metricConfiguration)).throwsException()
			.withMessage("Metric configuration with code \"" + code + "\" already exists in the configuration.");
	}

	private void metricsInSameConfigurationShouldNotHaveDuplicateMetrics() {
		CompoundMetric sc = loadFixture("sc", CompoundMetric.class);
		MetricConfiguration metricConfiguration = new MetricConfiguration(sc);
		assertThat(addMetricConfiguration(metricConfiguration)).throwsException()
			.withMessage("Metric already exists in the configuration: " + sc);
	}

	private VoidTask addMetricConfiguration(final MetricConfiguration metricConfiguration) {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.addMetricConfiguration(metricConfiguration);
			}
		};
	}

	private void shouldValidateScripts() {
		configuration.validateScripts();

		CompoundMetric sc = configuration.getCompoundMetrics().first();
		sc.setScript("return null;");
		assertThat(validateScripts()).throwsException().withMessage("Error evaluating Javascript for: sc");
	}

	private VoidTask validateScripts() {
		return new VoidTask() {

			@Override
			protected void perform() {
				configuration.validateScripts();
			}
		};
	}

	@Test
	public void shouldImportAndExportAsYaml() throws Exception {
		configuration.exportTo(file);
		String expectedYaml = loadResource("Configuration-sc.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(configuration, Configuration.importFrom(file));
	}
}