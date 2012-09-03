package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;
import org.kalibro.core.persistence.dao.DaoFactory;

public class DatabaseDaoFactory implements DaoFactory {

	private EntityManagerFactory entityManagerFactory;

	public DatabaseDaoFactory() {
		entityManagerFactory = Persistence.createEntityManagerFactory("Kalibro", getPersistenceProperties());
		getBaseToolDao().saveBaseTools();
	}

	protected Map<String, String> getPersistenceProperties() {
		DatabaseSettings settings = KalibroSettings.load().getServerSettings().getDatabaseSettings();
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, Environment.ddlGeneration());
		persistenceProperties.put(JDBC_DRIVER, settings.getDatabaseType().getDriverClassName());
		persistenceProperties.put(JDBC_URL, settings.getJdbcUrl());
		persistenceProperties.put(JDBC_USER, settings.getUsername());
		persistenceProperties.put(JDBC_PASSWORD, settings.getPassword());
		return persistenceProperties;
	}

	@Override
	public BaseToolDatabaseDao getBaseToolDao() {
		return new BaseToolDatabaseDao(createDatabaseManager());
	}

	@Override
	public ConfigurationDatabaseDao getConfigurationDao() {
		return new ConfigurationDatabaseDao(createDatabaseManager());
	}

	@Override
	public MetricConfigurationDatabaseDao getMetricConfigurationDao() {
		return new MetricConfigurationDatabaseDao(createDatabaseManager());
	}

	@Override
	public ProjectDatabaseDao getProjectDao() {
		return new ProjectDatabaseDao(createDatabaseManager());
	}

	@Override
	public ProjectResultDatabaseDao getProjectResultDao() {
		return new ProjectResultDatabaseDao(createDatabaseManager());
	}

	@Override
	public ModuleResultDatabaseDao getModuleResultDao() {
		return new ModuleResultDatabaseDao(createDatabaseManager());
	}

	private DatabaseManager createDatabaseManager() {
		return new DatabaseManager(entityManagerFactory.createEntityManager());
	}

	@Override
	protected void finalize() throws Throwable {
		entityManagerFactory.close();
		super.finalize();
	}
}