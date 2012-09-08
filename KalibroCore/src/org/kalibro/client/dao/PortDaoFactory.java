package org.kalibro.client.dao;

import org.kalibro.core.dao.*;

public class PortDaoFactory extends DaoFactory {

	private String serviceAddress;

	public PortDaoFactory(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@Override
	public BaseToolDao createBaseToolDao() {
		return new BaseToolPortDao();
	}

	@Override
	public ConfigurationDao createConfigurationDao() {
		return new ConfigurationPortDao();
	}

	@Override
	public MetricConfigurationDao createMetricConfigurationDao() {
		return new MetricConfigurationPortDao();
	}

	@Override
	public ModuleResultDao createModuleResultDao() {
		return new ModuleResultPortDao();
	}

	@Override
	public ProjectDao createProjectDao() {
		return new ProjectPortDao();
	}

	@Override
	public ProjectResultDao createProjectResultDao() {
		return new ProjectResultPortDao();
	}

	@Override
	protected ReadingDao createReadingDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected ReadingGroupDao createReadingGroupDao() {
		// TODO Auto-generated method stub
		return null;
	}
}