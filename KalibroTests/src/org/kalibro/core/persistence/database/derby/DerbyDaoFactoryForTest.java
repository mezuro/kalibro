package org.kalibro.core.persistence.database.derby;

import java.io.File;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.database.DatabaseDaoFactoryForTest;

class DerbyDaoFactoryForTest extends DatabaseDaoFactoryForTest {

	@Override
	protected DatabaseSettings getSettings() {
		new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(SupportedDatabase.APACHE_DERBY);
		settings.setJdbcUrl("jdbc:derby:memory:kalibro_test;create=true");
		settings.setUsername("");
		settings.setPassword("");
		return settings;
	}
}