package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.ConfigurationDatabaseTest;

public class ConfigurationMysqlTest extends ConfigurationDatabaseTest {

	@Override
	protected MysqlTestSettings getTestSettings() {
		return new MysqlTestSettings();
	}
}