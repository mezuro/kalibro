package org.kalibro.core.persistence;

import static org.eclipse.persistence.config.PersistenceUnitProperties.*;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
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

	private static EntityManager entityManager;
	private static DatabaseSettings currentSettings;

	static RecordManager createRecordManager() {
		return new RecordManager(entityManager);
	}

	public DatabaseDaoFactory() {
		this(KalibroSettings.load().getServerSettings().getDatabaseSettings());
	}

	public DatabaseDaoFactory(DatabaseSettings settings) {
		if (!settings.deepEquals(currentSettings))
			updateSettings(settings);
	}

	private void updateSettings(DatabaseSettings settings) {
		currentSettings = settings;
		Map<String, String> properties = new HashMap<String, String>();
		properties.put(DDL_GENERATION, Environment.ddlGeneration());
		properties.put(JDBC_DRIVER, settings.getDatabaseType().getDriverClassName());
		properties.put(JDBC_URL, settings.getJdbcUrl());
		properties.put(JDBC_USER, settings.getUsername());
		properties.put(JDBC_PASSWORD, settings.getPassword());
		properties.put(LOGGING_LOGGER, PersistenceLogger.class.getName());
		updateEntityManager(properties);
	}

	private void updateEntityManager(Map<String, String> properties) {
		if (entityManager != null)
			entityManager.getEntityManagerFactory().close();
		entityManager = Persistence.createEntityManagerFactory("Kalibro", properties).createEntityManager();
	}

	@Override
	protected BaseToolDatabaseDao createBaseToolDao() {
		return new BaseToolDatabaseDao();
	}

	@Override
	public ConfigurationDatabaseDao createConfigurationDao() {
		return new ConfigurationDatabaseDao();
	}

	@Override
	protected MetricConfigurationDatabaseDao createMetricConfigurationDao() {
		return new MetricConfigurationDatabaseDao();
	}

	@Override
	protected MetricResultDatabaseDao createMetricResultDao() {
		return new MetricResultDatabaseDao();
	}

	@Override
	public ModuleResultDatabaseDao createModuleResultDao() {
		return new ModuleResultDatabaseDao();
	}

	@Override
	public ProcessingDatabaseDao createProcessingDao() {
		return new ProcessingDatabaseDao();
	}

	@Override
	protected ProjectDatabaseDao createProjectDao() {
		return new ProjectDatabaseDao();
	}

	@Override
	protected RangeDatabaseDao createRangeDao() {
		return new RangeDatabaseDao();
	}

	@Override
	protected ReadingDatabaseDao createReadingDao() {
		return new ReadingDatabaseDao();
	}

	@Override
	protected ReadingGroupDatabaseDao createReadingGroupDao() {
		return new ReadingGroupDatabaseDao();
	}

	@Override
	protected RepositoryDatabaseDao createRepositoryDao() {
		return new RepositoryDatabaseDao();
	}
}