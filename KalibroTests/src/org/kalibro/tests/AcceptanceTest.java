package org.kalibro.tests;

import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.junit.AfterClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.powermock.reflect.Whitebox;

@RunWith(Theories.class)
public abstract class AcceptanceTest extends IntegrationTest {

	@DataPoints
	public static SupportedDatabase[] supportedDatabases() {
		return SupportedDatabase.values();
	}

	@AfterClass
	public static void deleteSettings() {
		new File(System.getProperty("user.dir") + "/derby.log").delete();
		new File(dotKalibro(), "kalibro.settings").delete();
	}

	protected void prepareSettings(SupportedDatabase databaseType) {
		KalibroSettings settings = new KalibroSettings();
		DatabaseSettings databaseSettigs = loadFixture(databaseType.name(), DatabaseSettings.class);
		settings.getServerSettings().setDatabaseSettings(databaseSettigs);
		settings.save();
		Whitebox.setInternalState(DatabaseDaoFactory.class, "currentSettings", (DatabaseSettings) null);
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(25000);
	}
}