package org.kalibro.core.persistence.database.derby;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.util.Map;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.database.DatabaseDaoFactoryForTest;

class DerbyDaoFactoryForTest extends DatabaseDaoFactoryForTest {

	protected DerbyDaoFactoryForTest() {
		super(new DatabaseSettings());
		new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
	}

	@Override
	protected Map<String, String> getPersistenceProperties(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = super.getPersistenceProperties(settings);
		persistenceProperties.put(JDBC_DRIVER, SupportedDatabase.APACHE_DERBY.getDriverClassName());
		persistenceProperties.put(JDBC_URL, "jdbc:derby:memory:kalibro_test;create=true");
		persistenceProperties.remove(JDBC_USER);
		persistenceProperties.remove(JDBC_PASSWORD);
		return persistenceProperties;
	}
}