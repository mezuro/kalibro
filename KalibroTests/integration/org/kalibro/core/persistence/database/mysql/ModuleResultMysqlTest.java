package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ModuleResultDatabaseTest;

public class ModuleResultMysqlTest extends ModuleResultDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}