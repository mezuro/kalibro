package org.kalibro.client.dao;

import org.kalibro.core.dao.*;

public class PortDaoFactory extends DaoFactory {

	private String serviceAddress;

	public PortDaoFactory(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@Override
	public BaseToolDao createBaseToolDao() {
		return new BaseToolPortDao(serviceAddress);
	}

	@Override
	public ConfigurationDao createConfigurationDao() {
		return new ConfigurationPortDao(serviceAddress);
	}

	@Override
	public MetricConfigurationDao createMetricConfigurationDao() {
		return new MetricConfigurationPortDao(serviceAddress);
	}

	@Override
	public ModuleResultDao createModuleResultDao() {
		return new ModuleResultPortDao(serviceAddress);
	}

	@Override
	public ProjectDao createProjectDao() {
		return new ProjectPortDao(serviceAddress);
	}

	@Override
	public ProjectResultDao createProjectResultDao() {
		return new ProjectResultPortDao(serviceAddress);
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