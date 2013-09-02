package org.kalibro.tests;

import static java.util.concurrent.TimeUnit.*;
import static org.kalibro.core.Environment.*;

import java.io.File;

import org.junit.AfterClass;
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

	@AfterClass
	public static void deleteSettingsFile() {
		new File(dotKalibro(), "kalibro.settings").delete();
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

	protected static void prepareSettings(SupportedDatabase databaseType) {
		KalibroSettings settings = new KalibroSettings();
		DatabaseSettings databaseSettings = loadFixture(databaseType.name(), DatabaseSettings.class);
		settings.getServerSettings().setDatabaseSettings(databaseSettings);
		settings.save();
	}

	@Override
	protected TestTimeout testTimeout() {
		return new TestTimeout(2, MINUTES);
	}
}