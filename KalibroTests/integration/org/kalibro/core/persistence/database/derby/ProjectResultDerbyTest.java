package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ProjectResultDatabaseTest;

public class ProjectResultDerbyTest extends ProjectResultDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}