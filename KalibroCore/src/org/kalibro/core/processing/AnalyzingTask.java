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

	private SourceTreeBuilder treeBuilder;
	private Configuration configuration;
	private MetricResultDatabaseDao metricResultDao;
	private ModuleResultDatabaseDao moduleResultDao;

	@Override
	protected void perform() {
		DatabaseDaoFactory daoFactory = daoFactory();
		metricResultDao = daoFactory.createMetricResultDao();
		moduleResultDao = daoFactory.createModuleResultDao();
		configuration = daoFactory.createConfigurationDao().snapshotFor(processing().getId());
		treeBuilder = new SourceTreeBuilder(processing().getId(), repository().getName(), moduleResultDao);
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
			configure(moduleResult);
		if (height > 0)
			configureFrom(height - 1);
	}

	private void configure(ModuleResult moduleResult) {
		CompoundResultCalculator configurer = new CompoundResultCalculator(moduleResult, configuration);
		for (MetricResult compoundResult : configurer.calculateCompoundMetricResults())
			metricResultDao.save(compoundResult, moduleResult.getId());
		moduleResult.setGrade(configurer.calculateGrade());
		moduleResultDao.save(moduleResult, processing().getId());
		if (moduleResult.hasParent())
			addResultsToParentOf(moduleResult);
		else
			processing().setResultsRoot(moduleResult);
	}

	private void addResultsToParentOf(ModuleResult moduleResult) {
		for (MetricResult metricResult : moduleResult.getMetricResults())
			addResultsToParent(moduleResult.getParent(), metricResult);
	}

	private void addResultsToParent(ModuleResult parent, MetricResult metricResult) {
		MetricConfiguration snapshot = metricResult.getConfiguration();
		Metric metric = metricResult.getMetric();
		Double value = metricResult.getValue();

		Long snapshotId = snapshot.getId();
		Long parentId = parent.getId();

		if (! (parent.hasResultFor(metric) || metric.isCompound()))
			metricResultDao.save(new MetricResult(snapshot, Double.NaN), parentId);
		if (value != Double.NaN)
			metricResultDao.addDescendantResult(value, parentId, snapshotId);
		for (Double descendantValue : metricResult.getDescendantResults())
			metricResultDao.addDescendantResult(descendantValue, parentId, snapshotId);
	}
}