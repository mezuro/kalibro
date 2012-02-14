package org.kalibro.core.persistence.database.derby;

import org.kalibro.core.persistence.database.ConfigurationDatabaseTest;

public class ConfigurationDerbyTest extends ConfigurationDatabaseTest {

	@Override
	protected DerbyTestSettings getTestSettings() {
		return new DerbyTestSettings();
	}
}