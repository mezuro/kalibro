package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.LOGGING_LOGGER;

import java.util.Map;

import org.kalibro.DatabaseSettings;

public abstract class DatabaseDaoFactoryForTest extends DatabaseDaoFactory {

	protected DatabaseDaoFactoryForTest(DatabaseSettings settings) {
		super(settings);
	}

	@Override
	protected Map<String, String> toPersistenceProperties(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = super.toPersistenceProperties(settings);
		persistenceProperties.put(LOGGING_LOGGER, NullPersistenceLogger.class.getName());
		return persistenceProperties;
	}
}