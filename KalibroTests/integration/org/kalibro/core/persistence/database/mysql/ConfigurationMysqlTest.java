package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ConfigurationDatabaseTest;

public class ConfigurationMysqlTest extends ConfigurationDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}