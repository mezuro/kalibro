package org.kalibro.core.persistence.database.mysql;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.database.DatabaseDaoFactoryForTest;

class MysqlDaoFactoryForTest extends DatabaseDaoFactoryForTest {

	@Override
	protected DatabaseSettings getSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(SupportedDatabase.MYSQL);
		settings.setJdbcUrl("jdbc:mysql://localhost:3306/kalibro_test");
		settings.setUsername("kalibro");
		settings.setPassword("kalibro");
		return settings;
	}
}