package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.DatabaseSettings;
import org.kalibro.core.Environment;

public abstract class DatabaseDaoFactoryForTest extends DatabaseDaoFactory {

	@Override
	protected Map<String, String> getPersistenceProperties() {
		DatabaseSettings settings = getSettings();
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, Environment.ddlGeneration());
		persistenceProperties.put(JDBC_DRIVER, settings.getDatabaseType().getDriverClassName());
		persistenceProperties.put(JDBC_URL, settings.getJdbcUrl());
		persistenceProperties.put(JDBC_USER, settings.getUsername());
		persistenceProperties.put(JDBC_PASSWORD, settings.getPassword());
		persistenceProperties.put(LOGGING_LOGGER, NullPersistenceLogger.class.getName());
		return persistenceProperties;
	}

	protected abstract DatabaseSettings getSettings();
}