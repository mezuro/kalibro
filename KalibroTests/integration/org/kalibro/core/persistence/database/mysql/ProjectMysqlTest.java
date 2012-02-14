package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ProjectDatabaseTest;

public class ProjectMysqlTest extends ProjectDatabaseTest {

	@Override
	protected MysqlTestSettings getTestSettings() {
		return new MysqlTestSettings();
	}
}