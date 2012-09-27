package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;

@RunWith(Parameterized.class)
public class ConfigurationAcceptanceTest extends AcceptanceTest {

	private File file;
	private Configuration configuration;

	public ConfigurationAcceptanceTest(SupportedDatabase databaseType) {
		super(databaseType);
	}

	@Before
	public void setUp() {
		configuration = loadFixture("analizo", Configuration.class);
		file = new File(Environment.dotKalibro(), "Configuration-exported.yml");
		file.deleteOnExit();
	}

	@After
	public void tearDown() {
		for (Configuration each : Configuration.all())
			each.delete();
	}

	@Test
	public void testCrud() {
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

	@Test
	public void nameShouldBeRequiredAndUnique() {
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
		shouldValidateScripts();
	}

	private void metricsInSameConfigurationShouldNotHaveDuplicateCodes() {
		String code = configuration.getMetricConfigurations().first().getCode();
		MetricConfiguration metricConfiguration = new MetricConfiguration();
		metricConfiguration.setCode(code);
		assertThat(addMetricConfiguration(metricConfiguration)).throwsException()
			.withMessage("Metric configuration with code \"" + code + "\" already exists in the configuration.");
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
		CompoundMetric compoundMetric = new CompoundMetric("Parameters per class");
		compoundMetric.setScript("return anpm * loc;");
		MetricConfiguration metricConfiguration = new MetricConfiguration(compoundMetric);
		metricConfiguration.setCode("ppc");
		configuration.addMetricConfiguration(metricConfiguration);
		configuration.validateScripts();

		compoundMetric.setScript("return null;");
		assertThat(validateScripts()).throwsException().withMessage("Error evaluating Javascript for: ppc");
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
		String expectedYaml = loadResource("Configuration-analizo.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(configuration, Configuration.importFrom(file));
	}
}