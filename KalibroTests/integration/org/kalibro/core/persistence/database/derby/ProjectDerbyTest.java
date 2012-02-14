package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ProjectDatabaseTest;

public class ProjectDerbyTest extends ProjectDatabaseTest {

	@Override
	protected DerbyTestSettings getTestSettings() {
		return new DerbyTestSettings();
	}
}