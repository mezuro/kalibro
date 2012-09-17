package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.ProjectResultDatabaseTest;

public class ProjectResultMysqlTest extends ProjectResultDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}