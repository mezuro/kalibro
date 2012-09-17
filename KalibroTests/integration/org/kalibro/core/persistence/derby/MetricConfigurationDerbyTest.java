package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.MetricConfigurationDatabaseTest;

public class MetricConfigurationDerbyTest extends MetricConfigurationDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}