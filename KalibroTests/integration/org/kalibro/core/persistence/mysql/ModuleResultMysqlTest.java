package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.ModuleResultDatabaseTest;

public class ModuleResultMysqlTest extends ModuleResultDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}