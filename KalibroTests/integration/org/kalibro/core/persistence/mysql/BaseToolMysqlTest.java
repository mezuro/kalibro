package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.BaseToolDatabaseTest;

public class BaseToolMysqlTest extends BaseToolDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}