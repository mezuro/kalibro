package org.kalibro;

import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.rules.Timeout;

public abstract class AcceptanceTest extends TestCase {

	private static final SupportedDatabase DATABASE = SupportedDatabase.APACHE_DERBY;

	protected static File settingsFile;

	@BeforeClass
	public static void prepareSettings() {
		KalibroSettings settings = new KalibroSettings();
		settings.getServerSettings().setDatabaseSettings(getTestSetttings());
		settings.save();

		settingsFile = new File(dotKalibro(), "kalibro.settings");
	}

	@AfterClass
	public static void deleteSettings() {
		settingsFile.delete();
	}

	private static DatabaseSettings getTestSetttings() {
		switch (DATABASE) {
			case APACHE_DERBY:
				new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
				return newSettings("jdbc:derby:memory:kalibro_test;create=true", false);
			case MYSQL:
				return newSettings("jdbc:mysql://localhost:3306/kalibro_test", true);
		}
		return null;
	}

	private static DatabaseSettings newSettings(String jdbcUrl, boolean hasUser) {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(DATABASE);
		settings.setJdbcUrl(jdbcUrl);
		settings.setUsername(hasUser ? "kalibro" : "");
		settings.setPassword(hasUser ? "kalibro" : "");
		return settings;
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(Timeouts.ACCEPTANCE_TIMEOUT);
	}
}