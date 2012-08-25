package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.MetricConfigurationDatabaseTest;

public class MetricConfigurationDerbyTest extends MetricConfigurationDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}