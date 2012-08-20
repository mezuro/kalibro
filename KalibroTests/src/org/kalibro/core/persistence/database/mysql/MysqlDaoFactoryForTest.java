package org.kalibro.core.persistence.database.mysql;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.Map;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.database.DatabaseDaoFactoryForTest;

class MysqlDaoFactoryForTest extends DatabaseDaoFactoryForTest {

	protected MysqlDaoFactoryForTest() {
		super(new DatabaseSettings());
	}

	@Override
	protected Map<String, String> getPersistenceProperties(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = super.getPersistenceProperties(settings);
		persistenceProperties.put(JDBC_DRIVER, SupportedDatabase.MYSQL.getDriverClassName());
		persistenceProperties.put(JDBC_URL, "jdbc:mysql://localhost:3306/kalibro_test");
		return persistenceProperties;
	}
}