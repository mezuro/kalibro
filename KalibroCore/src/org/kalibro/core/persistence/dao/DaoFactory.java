package org.kalibro.core.persistence.dao;

public abstract class DaoFactory {

	public abstract BaseToolDao createBaseToolDao();

	public abstract ConfigurationDao createConfigurationDao();

	public abstract MetricConfigurationDao createMetricConfigurationDao();

	public abstract ModuleResultDao createModuleResultDao();

	public abstract ProjectDao createProjectDao();

	public abstract ProjectResultDao createProjectResultDao();
}