package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.BaseToolDatabaseTest;

public class BaseToolDerbyTest extends BaseToolDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}