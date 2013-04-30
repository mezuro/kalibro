package org.kalibro.dao;

import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.client.ClientDaoFactory;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Abstract factory of data access objects (DAOs). Statically creates DAOs using the correct factory according to
 * current settings (see {@link KalibroSettings}).
 * 
 * @author Carlos Morais
 */
public abstract class DaoFactory {

	public static BaseToolDao getBaseToolDao() {
		return createFactory().createBaseToolDao();
	}

	public static ConfigurationDao getConfigurationDao() {
		return createFactory().createConfigurationDao();
	}

	public static MetricConfigurationDao getMetricConfigurationDao() {
		return createFactory().createMetricConfigurationDao();
	}

	public static MetricResultDao getMetricResultDao() {
		return createFactory().createMetricResultDao();
	}

	public static ModuleResultDao getModuleResultDao() {
		return createFactory().createModuleResultDao();
	}

	public static ProcessingDao getProcessingDao() {
		return createFactory().createProcessingDao();
	}

	public static ProjectDao getProjectDao() {
		return createFactory().createProjectDao();
	}

	public static RangeDao getRangeDao() {
		return createFactory().createRangeDao();
	}

	public static ReadingDao getReadingDao() {
		return createFactory().createReadingDao();
	}

	public static ReadingGroupDao getReadingGroupDao() {
		return createFactory().createReadingGroupDao();
	}

	public static RepositoryDao getRepositoryDao() {
		return createFactory().createRepositoryDao();
	}
	
	public static ProcessingObserverDao getProcessingObserverDao() {
		return createFactory().createProcessingObserverDao();
	}

	private static DaoFactory createFactory() {
		KalibroSettings settings = KalibroSettings.load();
		String serviceAddress = settings.getClientSettings().getServiceAddress();
		DatabaseSettings databaseSettings = settings.getServerSettings().getDatabaseSettings();
		return settings.clientSide() ? new ClientDaoFactory(serviceAddress) : new DatabaseDaoFactory(databaseSettings);
	}

	protected abstract BaseToolDao createBaseToolDao();

	protected abstract ConfigurationDao createConfigurationDao();

	protected abstract MetricConfigurationDao createMetricConfigurationDao();

	protected abstract MetricResultDao createMetricResultDao();

	protected abstract ModuleResultDao createModuleResultDao();

	protected abstract ProcessingDao createProcessingDao();

	protected abstract ProjectDao createProjectDao();

	protected abstract RangeDao createRangeDao();

	protected abstract ReadingDao createReadingDao();

	protected abstract ReadingGroupDao createReadingGroupDao();

	protected abstract RepositoryDao createRepositoryDao();
	
	protected abstract ProcessingObserverDao createProcessingObserverDao();
}