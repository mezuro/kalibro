package org.kalibro;

import static org.kalibro.SupportedDatabase.MYSQL;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;

import org.junit.BeforeClass;

public abstract class AcceptanceTest extends TestCase {

	private static final SupportedDatabase DATABASE = MYSQL;

	protected static File settingsFile;

	@BeforeClass
	public static void prepareSettings() {
		KalibroSettings settings = new KalibroSettings();
		settings.getServerSettings().setDatabaseSettings(getTestSetttings());
		settings.save();

		settingsFile = new File(dotKalibro(), "kalibro.settings");
		settingsFile.deleteOnExit();
	}

	private static DatabaseSettings getTestSetttings() {
		switch (DATABASE) {
			case APACHE_DERBY:
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
}