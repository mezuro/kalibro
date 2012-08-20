package org.kalibro.core.persistence.database;

import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kalibro.DatabaseSettings;
import org.kalibro.core.persistence.dao.DaoFactory;

public class DatabaseDaoFactory implements DaoFactory {

	private EntityManagerFactory entityManagerFactory;

	public DatabaseDaoFactory() {
		this(new DatabaseSettings());
	}

	public DatabaseDaoFactory(DatabaseSettings databaseSettings) {
		Map<String, String> persistenceProperties = databaseSettings.toPersistenceProperties();
		entityManagerFactory = Persistence.createEntityManagerFactory("Kalibro", persistenceProperties);
		getBaseToolDao().saveBaseTools();
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