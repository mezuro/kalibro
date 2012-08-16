package org.kalibro.core.persistence.database;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.Map;

import org.kalibro.core.settings.DatabaseSettings;

public abstract class DatabaseTestSettings extends DatabaseSettings {

	@Override
	public Map<String, String> toPersistenceProperties() {
		Map<String, String> persistenceProperties = super.toPersistenceProperties();
		persistenceProperties.put(LOGGING_LOGGER, NullPersistenceLogger.class.getName());
		return persistenceProperties;
	}
}