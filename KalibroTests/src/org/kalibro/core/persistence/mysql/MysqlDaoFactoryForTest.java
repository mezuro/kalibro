package org.kalibro.core.persistence.mysql;

import org.kalibro.DatabaseSettings;
import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.DatabaseDaoFactory;

class MysqlDaoFactoryForTest extends DatabaseDaoFactory {

	private static DatabaseSettings mysqlTestSettings() {
		DatabaseSettings settings = new DatabaseSettings();
		settings.setDatabaseType(SupportedDatabase.MYSQL);
		settings.setJdbcUrl("jdbc:mysql://localhost:3306/kalibro_test");
		settings.setUsername("kalibro");
		settings.setPassword("kalibro");
		return settings;
	}

	protected MysqlDaoFactoryForTest() {
		super(mysqlTestSettings());
	}
}