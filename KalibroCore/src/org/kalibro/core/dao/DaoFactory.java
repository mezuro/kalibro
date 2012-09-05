package org.kalibro.core.dao;

import org.kalibro.DatabaseSettings;
import org.kalibro.KalibroSettings;
import org.kalibro.client.dao.PortDaoFactory;
import org.kalibro.core.persistence.DatabaseDaoFactory;

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

	public static ReadingDao getReadingDao() {
		return getFactory().createReadingDao();
	}

	public static ReadingGroupDao getReadingGroupDao() {
		return getFactory().createReadingGroupDao();
	}

	private static DaoFactory getFactory() {
		KalibroSettings settings = KalibroSettings.load();
		DatabaseSettings databaseSettings = settings.getServerSettings().getDatabaseSettings();
		return settings.clientSide() ? new PortDaoFactory() : new DatabaseDaoFactory(databaseSettings);
	}

	protected abstract BaseToolDao createBaseToolDao();

	protected abstract ConfigurationDao createConfigurationDao();

	protected abstract MetricConfigurationDao createMetricConfigurationDao();

	protected abstract ModuleResultDao createModuleResultDao();

	protected abstract ProjectDao createProjectDao();

	protected abstract ProjectResultDao createProjectResultDao();

	protected ReadingDao createReadingDao() {
		// TODO Auto-generated method stub
		return null;
	}

	protected ReadingGroupDao createReadingGroupDao() {
		// TODO Auto-generated method stub
		return null;
	}
}