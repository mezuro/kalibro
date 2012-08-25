package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ProjectResultDatabaseTest;

public class ProjectResultMysqlTest extends ProjectResultDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}