package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ConfigurationDatabaseTest;

public class ConfigurationDerbyTest extends ConfigurationDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}