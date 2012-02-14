package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ModuleResultDatabaseTest;

public class ModuleResultDerbyTest extends ModuleResultDatabaseTest {

	@Override
	protected DerbyTestSettings getTestSettings() {
		return new DerbyTestSettings();
	}
}