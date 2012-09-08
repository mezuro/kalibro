package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kalibro.DatabaseSettings;
import org.kalibro.core.Environment;
import org.kalibro.core.dao.DaoFactory;

public class DatabaseDaoFactory extends DaoFactory {

	private EntityManagerFactory entityManagerFactory;

	public DatabaseDaoFactory(DatabaseSettings settings) {
		entityManagerFactory = Persistence.createEntityManagerFactory("Kalibro", toPersistenceProperties(settings));
		createBaseToolDao().saveBaseTools();
	}

	protected Map<String, String> toPersistenceProperties(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, Environment.ddlGeneration());
		persistenceProperties.put(JDBC_DRIVER, settings.getDatabaseType().getDriverClassName());
		persistenceProperties.put(JDBC_URL, settings.getJdbcUrl());
		persistenceProperties.put(JDBC_USER, settings.getUsername());
		persistenceProperties.put(JDBC_PASSWORD, settings.getPassword());
		return persistenceProperties;
	}

	@Override
	public BaseToolDatabaseDao createBaseToolDao() {
		return new BaseToolDatabaseDao(createDatabaseManager());
	}

	@Override
	public ConfigurationDatabaseDao createConfigurationDao() {
		return new ConfigurationDatabaseDao(createDatabaseManager());
	}

	@Override
	public MetricConfigurationDatabaseDao createMetricConfigurationDao() {
		return new MetricConfigurationDatabaseDao(createDatabaseManager());
	}

	@Override
	public ProjectDatabaseDao createProjectDao() {
		return new ProjectDatabaseDao(createDatabaseManager());
	}

	@Override
	public ProjectResultDatabaseDao createProjectResultDao() {
		return new ProjectResultDatabaseDao(createDatabaseManager());
	}

	@Override
	public ModuleResultDatabaseDao createModuleResultDao() {
		return new ModuleResultDatabaseDao(createDatabaseManager());
	}

	@Override
	public ReadingDatabaseDao createReadingDao() {
		return new ReadingDatabaseDao(createDatabaseManager());
	}

	@Override
	public ReadingGroupDatabaseDao createReadingGroupDao() {
		return new ReadingGroupDatabaseDao(createDatabaseManager());
	}

	private RecordManager createDatabaseManager() {
		return new RecordManager(entityManagerFactory.createEntityManager());
	}
}