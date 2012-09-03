package org.kalibro.core.persistence.dao;

public interface DaoFactory {

	BaseToolDao createBaseToolDao();

	ConfigurationDao createConfigurationDao();

	MetricConfigurationDao createMetricConfigurationDao();

	ModuleResultDao createModuleResultDao();

	ProjectDao createProjectDao();

	ProjectResultDao createProjectResultDao();
}