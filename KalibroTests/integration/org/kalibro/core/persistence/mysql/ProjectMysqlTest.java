package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.ProjectDatabaseTest;

public class ProjectMysqlTest extends ProjectDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}