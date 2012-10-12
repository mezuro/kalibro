package org.kalibro.client;

import org.kalibro.dao.*;

/**
 * Factory which creates data access objects that work as clients to Kalibro Service end points.
 * 
 * @author Carlos Morais
 */
public class ClientDaoFactory extends DaoFactory {

	private String serviceAddress;

	public ClientDaoFactory(String serviceAddress) {
		this.serviceAddress = serviceAddress;
	}

	@Override
	public BaseToolDao createBaseToolDao() {
		return new BaseToolClientDao(serviceAddress);
	}

	@Override
	public ConfigurationDao createConfigurationDao() {
		return new ConfigurationClientDao(serviceAddress);
	}

	@Override
	public MetricConfigurationDao createMetricConfigurationDao() {
		return new MetricConfigurationClientDao(serviceAddress);
	}

	@Override
	public ModuleResultDao createModuleResultDao() {
		return new ModuleResultClientDao(serviceAddress);
	}

	@Override
	public ProjectDao createProjectDao() {
		return new ProjectClientDao(serviceAddress);
	}

	@Override
	public ProcessingDao createProjectResultDao() {
		return new ProcessingClientDao(serviceAddress);
	}

	@Override
	protected ReadingDao createReadingDao() {
		return new ReadingClientDao(serviceAddress);
	}

	@Override
	protected ReadingGroupDao createReadingGroupDao() {
		return new ReadingGroupClientDao(serviceAddress);
	}
}