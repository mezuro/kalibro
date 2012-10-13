package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.core.Environment;
import org.kalibro.dao.DaoFactory;

/**
 * Factory of data access objects that read and write on the database.
 * 
 * @author Carlos Morais
 */
public class DatabaseDaoFactory extends DaoFactory {

	private EntityManagerFactory entityManagerFactory;

	public DatabaseDaoFactory() {
		this(KalibroSettings.load().getServerSettings().getDatabaseSettings());
	}

	public DatabaseDaoFactory(DatabaseSettings settings) {
		Map<String, String> persistenceProperties = new HashMap<String, String>();
		persistenceProperties.put(DDL_GENERATION, Environment.ddlGeneration());
		persistenceProperties.put(JDBC_DRIVER, settings.getDatabaseType().getDriverClassName());
		persistenceProperties.put(JDBC_URL, settings.getJdbcUrl());
		persistenceProperties.put(JDBC_USER, settings.getUsername());
		persistenceProperties.put(JDBC_PASSWORD, settings.getPassword());
		persistenceProperties.put(LOGGING_LOGGER, PersistenceLogger.class.getName());
		entityManagerFactory = Persistence.createEntityManagerFactory("Kalibro", persistenceProperties);
	}

	@Override
	public BaseToolDatabaseDao createBaseToolDao() {
		return new BaseToolDatabaseDao();
	}

	@Override
	public ConfigurationDatabaseDao createConfigurationDao() {
		return new ConfigurationDatabaseDao(recordManager());
	}

	@Override
	public MetricConfigurationDatabaseDao createMetricConfigurationDao() {
		return new MetricConfigurationDatabaseDao(recordManager());
	}

	@Override
	public MetricResultDatabaseDao createMetricResultDao() {
		return new MetricResultDatabaseDao(recordManager());
	}

	@Override
	public ModuleResultDatabaseDao createModuleResultDao() {
		return new ModuleResultDatabaseDao(recordManager());
	}

	@Override
	public ProcessingDatabaseDao createProcessingDao() {
		return new ProcessingDatabaseDao(recordManager());
	}

	@Override
	public ProjectDatabaseDao createProjectDao() {
		return new ProjectDatabaseDao(recordManager());
	}

	@Override
	public RangeDatabaseDao createRangeDao() {
		return new RangeDatabaseDao(recordManager());
	}

	@Override
	public ReadingDatabaseDao createReadingDao() {
		return new ReadingDatabaseDao(recordManager());
	}

	@Override
	public ReadingGroupDatabaseDao createReadingGroupDao() {
		return new ReadingGroupDatabaseDao(recordManager());
	}

	@Override
	public RepositoryDatabaseDao createRepositoryDao() {
		return new RepositoryDatabaseDao(recordManager());
	}

	private RecordManager recordManager() {
		return new RecordManager(entityManagerFactory.createEntityManager());
	}
}