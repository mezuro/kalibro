package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.BaseToolDatabaseTest;

public class BaseToolMysqlTest extends BaseToolDatabaseTest {

	@Override
	protected MysqlTestSettings getTestSettings() {
		return new MysqlTestSettings();
	}
}