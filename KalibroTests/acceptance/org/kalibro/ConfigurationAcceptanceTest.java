package org.kalibro;

import static org.junit.Assert.*;

import java.io.File;
import java.util.TreeSet;

import javax.persistence.RollbackException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.theories.Theory;
import org.kalibro.core.Environment;
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
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
		configuration.setName(" ");
		assertSave().throwsException().withMessage("Configuration requires name.");
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
	public void deleteConfigurationShouldCascadeToMetricConfigurations(SupportedDatabase databaseType) {
		resetDatabase(databaseType);
		configuration.save();
		Long id = configuration.getId();

		configuration.delete();
		configuration.setMetricConfigurations(new TreeSet<MetricConfiguration>());
		Whitebox.setInternalState(configuration, "id", id);
		configuration.save();

		Configuration first = Configuration.all().first();
		assertEquals(id, first.getId());
		assertTrue(first.getMetricConfigurations().isEmpty());
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