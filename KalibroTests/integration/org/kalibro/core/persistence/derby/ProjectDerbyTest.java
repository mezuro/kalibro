package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.ProjectDatabaseTest;

public class ProjectDerbyTest extends ProjectDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}