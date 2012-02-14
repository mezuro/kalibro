package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ModuleResultDatabaseTest;

public class ModuleResultMysqlTest extends ModuleResultDatabaseTest {

	@Override
	protected MysqlTestSettings getTestSettings() {
		return new MysqlTestSettings();
	}
}