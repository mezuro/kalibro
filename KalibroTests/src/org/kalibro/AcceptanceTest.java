package org.kalibro;

import java.io.File;

import org.junit.BeforeClass;
import org.kalibro.core.Environment;
import org.kalibro.core.settings.KalibroSettings;

public abstract class AcceptanceTest extends KalibroTestCase {

	protected static File settingsFile;

	@BeforeClass
	public static void prepareSettings() {
		KalibroSettings settings = new KalibroSettings();
		settings.getServerSettings().getDatabaseSettings().setJdbcUrl("jdbc:mysql://localhost:3306/kalibro_test");
		settings.save();

		settingsFile = new File(Environment.dotKalibro(), "kalibro.settings");
		settingsFile.deleteOnExit();
	}
}