package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.BaseToolDatabaseTest;

public class BaseToolDerbyTest extends BaseToolDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}