package org.kalibro.core.persistence.database.mysql;

import org.kalibro.core.persistence.database.MetricConfigurationDatabaseTest;

public class MetricConfigurationMysqlTest extends MetricConfigurationDatabaseTest {

	@Override
	protected MysqlTestSettings getTestSettings() {
		return new MysqlTestSettings();
	}
}