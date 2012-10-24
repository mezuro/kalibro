package org.kalibro.client;

import org.kalibro.dao.*;

/**
 * Factory of data access objects that work as clients to Kalibro Service end points.
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
	public MetricResultDao createMetricResultDao() {
		return new MetricResultClientDao(serviceAddress);
	}

	@Override
	public ModuleResultDao createModuleResultDao() {
		return new ModuleResultClientDao(serviceAddress);
	}

	@Override
	public ProcessingDao createProcessingDao() {
		return new ProcessingClientDao(serviceAddress);
	}

	@Override
	public ProjectDao createProjectDao() {
		return new ProjectClientDao(serviceAddress);
	}

	@Override
	protected RangeDao createRangeDao() {
		return new RangeClientDao(serviceAddress);
	}

	@Override
	protected ReadingDao createReadingDao() {
		return new ReadingClientDao(serviceAddress);
	}

	@Override
	protected ReadingGroupDao createReadingGroupDao() {
		return new ReadingGroupClientDao(serviceAddress);
	}

	@Override
	protected RepositoryDao createRepositoryDao() {
		return new RepositoryClientDao(serviceAddress);
	}
}