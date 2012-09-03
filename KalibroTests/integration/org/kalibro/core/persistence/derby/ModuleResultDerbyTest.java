package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.ModuleResultDatabaseTest;

public class ModuleResultDerbyTest extends ModuleResultDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}