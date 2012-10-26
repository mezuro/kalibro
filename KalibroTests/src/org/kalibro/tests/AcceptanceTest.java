package org.kalibro.tests;

import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.rules.Timeout;
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
		DatabaseSettings databaseSettigs = loadFixture(databaseType.name(), DatabaseSettings.class);
		settings.getServerSettings().setDatabaseSettings(databaseSettigs);
		settings.save();
	}

	@AfterClass
	public static void deleteSettings() {
		new File(System.getProperty("user.dir") + "/derby.log").delete();
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

	@Override
	protected Timeout testTimeout() {
		return new Timeout(25000);
	}
}