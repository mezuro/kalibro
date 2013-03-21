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
	protected BaseToolDao createBaseToolDao() {
		return new BaseToolClientDao(serviceAddress);
	}

	@Override
	protected ConfigurationDao createConfigurationDao() {
		return new ConfigurationClientDao(serviceAddress);
	}

	@Override
	protected MetricConfigurationDao createMetricConfigurationDao() {
		return new MetricConfigurationClientDao(serviceAddress);
	}

	@Override
	protected MetricResultDao createMetricResultDao() {
		return new MetricResultClientDao(serviceAddress);
	}

	@Override
	protected ModuleResultDao createModuleResultDao() {
		return new ModuleResultClientDao(serviceAddress);
	}

	@Override
	protected ProcessingDao createProcessingDao() {
		return new ProcessingClientDao(serviceAddress);
	}

	@Override
	protected ProjectDao createProjectDao() {
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

	@Override
	protected ProcessingNotificationDao createProcessingNotificationDao() {
		return new ProcessingNotificationClientDao(serviceAddress);
	}
}