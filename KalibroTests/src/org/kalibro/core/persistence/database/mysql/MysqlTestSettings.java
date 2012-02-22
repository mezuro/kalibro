package org.kalibro.core.persistence.database.mysql;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.Map;

import org.kalibro.core.persistence.database.DatabaseTestSettings;
import org.kalibro.core.settings.SupportedDatabase;

class MysqlTestSettings extends DatabaseTestSettings {

	protected MysqlTestSettings() {
		super();
		setDatabaseType(SupportedDatabase.MYSQL);
	}

	@Override
	public Map<String, String> toPersistenceProperties() {
		Map<String, String> persistenceProperties = super.toPersistenceProperties();
		persistenceProperties.put(JDBC_URL, "jdbc:mysql://localhost:3306/kalibro_test");
		return persistenceProperties;
	}
}