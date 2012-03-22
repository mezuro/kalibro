package org.kalibro.client.dao;

import org.kalibro.core.persistence.dao.*;

public class PortDaoFactory implements DaoFactory {

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
	public BaseToolDao getBaseToolDao() {
		return baseToolDao;
	}

	@Override
	public ConfigurationDao getConfigurationDao() {
		return configurationDao;
	}

	@Override
	public MetricConfigurationDao getMetricConfigurationDao() {
		return metricConfigurationDao;
	}

	@Override
	public ProjectDao getProjectDao() {
		return projectDao;
	}

	@Override
	public ProjectResultDao getProjectResultDao() {
		return projectResultDao;
	}

	@Override
	public ModuleResultDao getModuleResultDao() {
		return moduleResultDao;
	}
}