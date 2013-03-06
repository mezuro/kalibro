package org.kalibro.core.processing;

import org.kalibro.*;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.MetricResultDatabaseDao;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Analyzes and saves metric results collected by {@link BaseTool}s.
 * 
 * @author Carlos Morais
 */
class AnalyzingTask extends ProcessSubtask {

	private Configuration configuration;
	private SourceTreeBuilder treeBuilder;
	private ModuleResultConfigurer configurer;

	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	@Override
	protected void perform() {
		DatabaseDaoFactory daoFactory = daoFactory();
		metricResultDao = daoFactory.createMetricResultDao();
		moduleResultDao = daoFactory.createModuleResultDao();
		configuration = daoFactory.createConfigurationDao().snapshotFor(processing().getId());
		treeBuilder = new SourceTreeBuilder(processing().getId(), repository().getName(), moduleResultDao);
		configurer = new ModuleResultConfigurer(processing(), configuration, metricResultDao, moduleResultDao);
		for (NativeModuleResult nativeModuleResult : resultProducer())
			addNativeResult(nativeModuleResult);
		configureFrom(treeBuilder.getMaximumHeight());
	}

	private void addNativeResult(NativeModuleResult nativeResult) {
		Long moduleResultId = treeBuilder.save(nativeResult.getModule());
		for (NativeMetricResult metricResult : nativeResult.getMetricResults())
			addMetricResult(metricResult, moduleResultId);
	}

	private void addMetricResult(NativeMetricResult nativeMetricResult, Long moduleResultId) {
		Metric metric = nativeMetricResult.getMetric();
		Double value = nativeMetricResult.getValue();
		MetricConfiguration snapshot = configuration.getConfigurationFor(metric);
		metricResultDao.save(new MetricResult(snapshot, value), moduleResultId);
	}

	private void configureFrom(int height) {
		for (ModuleResult moduleResult : moduleResultDao.getResultsAtHeight(height, processing().getId()))
			configurer.configure(moduleResult);
		if (height > 0)
			configureFrom(height - 1);
	}
}