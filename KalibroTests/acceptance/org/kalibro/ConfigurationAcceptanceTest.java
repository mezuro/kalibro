package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Set;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.MetricConfigurationDao;
import org.kalibro.tests.AcceptanceTest;
import org.powermock.reflect.Whitebox;

public class ConfigurationAcceptanceTest extends AcceptanceTest {

	private Configuration configuration;

	@Before
	public void setUp() {
		configuration = loadFixture("sc-analizo", Configuration.class);
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertNotSaved();

		configuration.save();
		assertSaved();

		configuration.setName("ConfigurationAcceptanceTest name");
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
		assertDeepEquals(set(configuration), Configuration.all());
	}

	@Theory
	public void shouldValidateOnSave(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		shouldValidateScripts();
		nameShouldBeUnique();
		nameShouldBeRequired();
	}

	private void shouldValidateScripts() {
		CompoundMetric sc = configuration.getCompoundMetrics().first();
		sc.setScript("return null;");
		assertSave().throwsException().withMessage("Error evaluating Javascript for: sc");
		sc.setScript("return cbo * lcom4;");
	}

	private void nameShouldBeUnique() {
		new Configuration(configuration.getName()).save();
		assertSave().doThrow(RollbackException.class);
	}

	private void nameShouldBeRequired() {
		String name = configuration.getName();
		configuration.setName(" ");
		assertSave().throwsException().withMessage("Configuration requires name.");
		configuration.setName(name);
	}

	private TaskMatcher assertSave() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		});
	}

	@Theory
	public void shouldCascadeSaveButNotDeleteToReadingGroups(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		assertTrue(ReadingGroup.all().isEmpty());
		configuration.save();
		assertFalse(ReadingGroup.all().isEmpty());
		configuration.delete();
		assertFalse(ReadingGroup.all().isEmpty());
	}

	@Theory
	public void deleteConfigurationShouldCascadeToMetricConfigurations(SupportedDatabase databaseType)
		throws Exception {
		resetDatabase(databaseType);
		configuration.save();
		assertEquals(configuration.getMetricConfigurations(), allMetricConfigurations());

		configuration.delete();
		assertTrue(allMetricConfigurations().isEmpty());
	}

	private Set<MetricConfiguration> allMetricConfigurations() throws Exception {
		MetricConfigurationDao metricConfigurationDao = DaoFactory.getMetricConfigurationDao();
		Set<MetricConfiguration> allMetricConfigurations = Whitebox.invokeMethod(metricConfigurationDao, "all");
		return allMetricConfigurations;
	}

	@Test
	public void shouldImportAndExportAsYaml() throws Exception {
		configuration = loadFixture("sc", Configuration.class);
		File file = new File(Environment.dotKalibro(), "Configuration-exported.yml");
		file.deleteOnExit();

		configuration.exportTo(file);
		assertEquals(loadResource("Configuration-sc.yml"), FileUtils.readFileToString(file));
		assertDeepEquals(configuration, Configuration.importFrom(file));
	}
}