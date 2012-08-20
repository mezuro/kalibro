package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.BaseToolDatabaseTest;

public class BaseToolMysqlTest extends BaseToolDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}