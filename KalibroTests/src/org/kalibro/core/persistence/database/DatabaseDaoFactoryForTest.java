package org.kalibro.core.persistence.database;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.Map;

import org.kalibro.DatabaseSettings;

public abstract class DatabaseDaoFactoryForTest extends DatabaseDaoFactory {

	public DatabaseDaoFactoryForTest(DatabaseSettings databaseSettings) {
		super(databaseSettings);
	}

	@Override
	protected Map<String, String> getPersistenceProperties(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = super.getPersistenceProperties(settings);
		persistenceProperties.put(LOGGING_LOGGER, NullPersistenceLogger.class.getName());
		return persistenceProperties;
	}
}