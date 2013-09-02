package org.kalibro.core.processing;

import java.util.ArrayList;
import java.util.List;

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
		moduleResultDao.aggregateResults(processing().getId());
		configureResults();
	}

	private void addNativeResult(NativeModuleResult nativeResult) {
		Long moduleResultId = treeBuilder.save(nativeResult.getModule());
		List<MetricResult> metricResults = new ArrayList<MetricResult>();
		for (NativeMetricResult metricResult : nativeResult.getMetricResults())
			metricResults.add(configureMetricResult(metricResult));
		metricResultDao.saveAll(metricResults, moduleResultId);
	}

	private MetricResult configureMetricResult(NativeMetricResult nativeMetricResult) {
		Metric metric = nativeMetricResult.getMetric();
		Double value = nativeMetricResult.getValue();
		MetricConfiguration snapshot = configuration.getConfigurationFor(metric);
		return new MetricResult(snapshot, value);
	}

	private void configureResults() {
		for (ModuleResult moduleResult : moduleResultDao.getResultsOfProcessing(processing().getId()))
			configurer.configure(moduleResult);
	}
}