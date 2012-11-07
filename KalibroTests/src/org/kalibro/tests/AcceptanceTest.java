package org.kalibro.tests;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.runner.RunWith;
import org.kalibro.*;

@RunWith(Theories.class)
public abstract class AcceptanceTest extends IntegrationTest {

	@DataPoints
	public static SupportedDatabase[] supportedDatabases() {
		return SupportedDatabase.values();
	}

	@BeforeClass
	public static void prepareSettings() {
		prepareSettings(SupportedDatabase.APACHE_DERBY);
	}

	private static void prepareSettings(SupportedDatabase databaseType) {
		KalibroSettings settings = new KalibroSettings();
		DatabaseSettings databaseSettings = loadFixture(databaseType.name(), DatabaseSettings.class);
		settings.getServerSettings().setDatabaseSettings(databaseSettings);
		settings.save();
	}

	@AfterClass
	public static void deleteGeneratedFiles() {
		File directory = new File(System.getProperty("user.dir"));
		new File(directory, "derby.log").delete();
		new File(directory, "kalibro.sqlite").delete();
		new File(dotKalibro(), "kalibro.settings").delete();
		FileUtils.deleteQuietly(projectsDirectory());
	}

	protected void resetDatabase(SupportedDatabase databaseType) {
		prepareSettings(databaseType);
		for (Project project : Project.all())
			project.delete();
		for (Configuration configuration : Configuration.all())
			configuration.delete();
		for (ReadingGroup group : ReadingGroup.all())
			group.delete();
	}

	@Override
	protected TestTimeout testTimeout() {
		return new TestTimeout(2, MINUTES);
	}
}