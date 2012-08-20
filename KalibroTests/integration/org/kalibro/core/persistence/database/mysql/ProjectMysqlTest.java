package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ProjectDatabaseTest;

public class ProjectMysqlTest extends ProjectDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}