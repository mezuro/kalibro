package org.kalibro.tests;

import static org.kalibro.SupportedDatabase.APACHE_DERBY;
import static org.kalibro.core.Environment.dotKalibro;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.rules.Timeout;
import org.junit.runners.Parameterized.Parameters;
import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.SupportedDatabase;

public abstract class AcceptanceTest extends IntegrationTest {

	@Parameters
	public static Collection<SupportedDatabase[]> supportedDatabases() {
		new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
		List<SupportedDatabase[]> parameters = new ArrayList<SupportedDatabase[]>();
		for (SupportedDatabase databaseType : SupportedDatabase.values())
			parameters.add(new SupportedDatabase[]{databaseType});
		return parameters;
	}

	public AcceptanceTest() {
		this(APACHE_DERBY);
	}

	public AcceptanceTest(SupportedDatabase databaseType) {
		KalibroSettings settings = new KalibroSettings();
		DatabaseSettings databaseSettigs = loadFixture(databaseType.name(), DatabaseSettings.class);
		settings.getServerSettings().setDatabaseSettings(databaseSettigs);
		settings.save();
	}

	@After
	public void deleteSettings() {
		new File(dotKalibro(), "kalibro.settings").delete();
	}

	@Override
	protected Timeout testTimeout() {
		return new Timeout(25000);
	}
}