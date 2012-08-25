package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ProjectDatabaseTest;

public class ProjectDerbyTest extends ProjectDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}