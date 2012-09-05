package org.kalibro.client.dao;

import org.kalibro.core.dao.*;

public class PortDaoFactory extends DaoFactory {

	private BaseToolDao baseToolDao;
	private ConfigurationDao configurationDao;
	private MetricConfigurationDao metricConfigurationDao;
	private ProjectDao projectDao;
	private ProjectResultDao projectResultDao;
	private ModuleResultDao moduleResultDao;

	public PortDaoFactory() {
		baseToolDao = new BaseToolPortDao();
		configurationDao = new ConfigurationPortDao();
		metricConfigurationDao = new MetricConfigurationPortDao();
		projectDao = new ProjectPortDao();
		projectResultDao = new ProjectResultPortDao();
		moduleResultDao = new ModuleResultPortDao();
	}

	@Override
	public BaseToolDao createBaseToolDao() {
		return baseToolDao;
	}

	@Override
	public ConfigurationDao createConfigurationDao() {
		return configurationDao;
	}

	@Override
	public MetricConfigurationDao createMetricConfigurationDao() {
		return metricConfigurationDao;
	}

	@Override
	public ProjectDao createProjectDao() {
		return projectDao;
	}

	@Override
	public ProjectResultDao createProjectResultDao() {
		return projectResultDao;
	}

	@Override
	public ModuleResultDao createModuleResultDao() {
		return moduleResultDao;
	}
}