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
		return getFactory().createBaseToolDao();
	}

	public static ConfigurationDao getConfigurationDao() {
		return getFactory().createConfigurationDao();
	}

	public static MetricConfigurationDao getMetricConfigurationDao() {
		return getFactory().createMetricConfigurationDao();
	}

	public static ModuleResultDao getModuleResultDao() {
		return getFactory().createModuleResultDao();
	}

	public static ProjectDao getProjectDao() {
		return getFactory().createProjectDao();
	}

	public static ProjectResultDao getProjectResultDao() {
		return getFactory().createProjectResultDao();
	}

	public static RangeDao getRangeDao() {
		// TODO
		return null;
	}

	public static ReadingDao getReadingDao() {
		return getFactory().createReadingDao();
	}

	public static ReadingGroupDao getReadingGroupDao() {
		return getFactory().createReadingGroupDao();
	}

	private static DaoFactory getFactory() {
		KalibroSettings settings = KalibroSettings.load();
		String serviceAddress = settings.getClientSettings().getServiceAddress();
		DatabaseSettings databaseSettings = settings.getServerSettings().getDatabaseSettings();
		return settings.clientSide() ? new ClientDaoFactory(serviceAddress) : new DatabaseDaoFactory(databaseSettings);
	}

	protected abstract BaseToolDao createBaseToolDao();

	protected abstract ConfigurationDao createConfigurationDao();

	protected abstract MetricConfigurationDao createMetricConfigurationDao();

	protected abstract ModuleResultDao createModuleResultDao();

	protected abstract ProjectDao createProjectDao();

	protected abstract ProjectResultDao createProjectResultDao();

	protected abstract ReadingDao createReadingDao();

	protected abstract ReadingGroupDao createReadingGroupDao();
}