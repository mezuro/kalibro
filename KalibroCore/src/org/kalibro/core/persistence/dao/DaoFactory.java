package org.kalibro.core.persistence.dao;

public interface DaoFactory {

	BaseToolDao getBaseToolDao();

	ConfigurationDao getConfigurationDao();

	MetricConfigurationDao getMetricConfigurationDao();

	ProjectDao getProjectDao();

	ProjectResultDao getProjectResultDao();

	ModuleResultDao getModuleResultDao();
}