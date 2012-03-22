package org.kalibro.client.dao;

import org.kalibro.core.persistence.dao.*;

public class PortDaoFactory implements DaoFactory {

	private BaseToolDao baseToolDao;
	private ConfigurationDao configurationDao;
	private ProjectDao projectDao;
	private ProjectResultDao projectResultDao;
	private ModuleResultDao moduleResultDao;

	public PortDaoFactory() {
		baseToolDao = new BaseToolPortDao();
		configurationDao = new ConfigurationPortDao();
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
		// TODO Auto-generated method stub
		return null;
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