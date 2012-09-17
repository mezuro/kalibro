package org.kalibro.core.persistence.mysql;

import org.kalibro.core.persistence.MetricConfigurationDatabaseTest;

public class MetricConfigurationMysqlTest extends MetricConfigurationDatabaseTest {

	@Override
	protected MysqlDaoFactoryForTest getDaoFactoryForTest() {
		return new MysqlDaoFactoryForTest();
	}
}