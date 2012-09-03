package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.ConfigurationDatabaseTest;

public class ConfigurationDerbyTest extends ConfigurationDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}