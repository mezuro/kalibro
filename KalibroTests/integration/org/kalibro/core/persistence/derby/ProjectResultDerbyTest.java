package org.kalibro.core.persistence.derby;

import org.kalibro.core.persistence.ProjectResultDatabaseTest;

public class ProjectResultDerbyTest extends ProjectResultDatabaseTest {

	@Override
	protected DerbyDaoFactoryForTest getDaoFactoryForTest() {
		return new DerbyDaoFactoryForTest();
	}
}