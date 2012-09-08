package org.kalibro.core.dao;

public class DummyDaoFactory extends DaoFactory {

	@Override
	protected BaseToolDao createBaseToolDao() {
		return null;
	}

	@Override
	protected ConfigurationDao createConfigurationDao() {
		return null;
	}

	@Override
	protected MetricConfigurationDao createMetricConfigurationDao() {
		return null;
	}

	@Override
	protected ModuleResultDao createModuleResultDao() {
		return null;
	}

	@Override
	protected ProjectDao createProjectDao() {
		return null;
	}

	@Override
	protected ProjectResultDao createProjectResultDao() {
		return null;
	}

	@Override
	protected ReadingDao createReadingDao() {
		return null;
	}

	@Override
	protected ReadingGroupDao createReadingGroupDao() {
		return null;
	}
}