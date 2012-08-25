package org.kalibro;

import static org.kalibro.core.Environment.*;

import java.io.File;

import org.junit.BeforeClass;

public abstract class AcceptanceTest extends TestCase {

	protected static File settingsFile;

	@BeforeClass
	public static void prepareSettings() {
		KalibroSettings settings = new KalibroSettings();
		settings.getServerSettings().getDatabaseSettings().setJdbcUrl("jdbc:mysql://localhost:3306/kalibro_test");
		settings.save();

		settingsFile = new File(dotKalibro(), "kalibro.settings");
		settingsFile.deleteOnExit();
	}
}