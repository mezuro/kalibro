package org.kalibro.core.persistence.database.derby;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.io.File;
import java.util.Map;

import org.kalibro.SupportedDatabase;
import org.kalibro.core.persistence.database.DatabaseTestSettings;

public class DerbyTestSettings extends DatabaseTestSettings {

	public DerbyTestSettings() {
		super();
		setDatabaseType(SupportedDatabase.APACHE_DERBY);
	}

	@Override
	public Map<String, String> toPersistenceProperties() {
		Map<String, String> persistenceProperties = super.toPersistenceProperties();
		persistenceProperties.put(JDBC_URL, "jdbc:derby:memory:kalibro_test;create=true");
		persistenceProperties.remove(JDBC_USER);
		persistenceProperties.remove(JDBC_PASSWORD);
		new File(System.getProperty("user.dir") + "/derby.log").deleteOnExit();
		return persistenceProperties;
	}
}