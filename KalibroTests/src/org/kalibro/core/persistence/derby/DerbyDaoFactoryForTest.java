package org.kalibro.core.persistence.derby;

import java.io.File;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.DatabaseDaoFactoryForTest;

class DerbyDaoFactoryForTest extends DatabaseDaoFactoryForTest {

	private static DatabaseSettings derbyTestSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(SupportedDatabase.APACHE_DERBY);
		settings.setJdbcUrl("jdbc:derby:memory:kalibro_test;create=true");
		settings.setUsername("");
		settings.setPassword("");
		return settings;
	}

	protected DerbyDaoFactoryForTest() {
		super(derbyTestSettings());
		new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
	}
}