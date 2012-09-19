package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.ConfigurationDatabaseTest;

public class ConfigurationMysqlTest extends ConfigurationDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}