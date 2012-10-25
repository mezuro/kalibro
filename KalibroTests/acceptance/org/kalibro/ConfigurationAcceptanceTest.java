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
import org.kalibro.core.concurrent.TaskMatcher;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.tests.AcceptanceTest;
import org.powermock.reflect.Whitebox;

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
		for (ReadingGroup group : ReadingGroup.all())
			group.delete();
	}

	@Theory
	public void testCrud(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		changeBaseTool();
		assertNotSaved();

		configuration.save();
		assertSaved();

		configuration.setDescription("ConfigurationAcceptanceTest description");
		assertFalse(Configuration.all().first().deepEquals(configuration));

		configuration.save();
		assertSaved();

		configuration.delete();
		assertNotSaved();
	}

	private void changeBaseTool() {
		BaseTool analizo = new BaseTool("org.analizo.AnalizoMetricCollector");
		for (MetricConfiguration each : configuration.getMetricConfigurations())
			if (!each.getMetric().isCompound())
				Whitebox.setInternalState(each, "baseTool", analizo);
	}

	private void assertNotSaved() {
		assertTrue(Configuration.all().isEmpty());
	}

	private void assertSaved() {
		assertDeepEquals(set(configuration), Configuration.all());
	}

	@Theory
	public void shouldValidateBeforeSave(SupportedDatabase databaseType) {
		changeDatabase(databaseType);
		nameShouldBeRequiredAndUnique();
		shouldValidateScripts();
	}

	private void nameShouldBeRequiredAndUnique() {
		configuration.setName(" ");
		assertSave().throwsException().withMessage("Configuration requires name.");

		configuration.setName("Unique");
		configuration.save();

		configuration = new Configuration("Unique");
		assertSave().doThrow(RollbackException.class);
	}

	private void shouldValidateScripts() {
		CompoundMetric sc = configuration.getCompoundMetrics().first();
		sc.setScript("return null;");
		assertSave().throwsException().withMessage("Error evaluating Javascript for: sc");
	}

	private TaskMatcher assertSave() {
		return assertThat(new VoidTask() {

			@Override
			protected void perform() {
				configuration.save();
			}
		});
	}

	@Test
	public void shouldImportAndExportAsYaml() throws Exception {
		configuration.exportTo(file);
		String expectedYaml = loadResource("Configuration-sc.yml");
		assertEquals(expectedYaml, FileUtils.readFileToString(file));
		assertDeepEquals(configuration, Configuration.importFrom(file));
	}
}