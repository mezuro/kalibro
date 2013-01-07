package org.kalibro.core.processing;

import java.util.Collection;

import org.kalibro.*;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Analyzes and saves metric results collected by {@link BaseTool}s.
 * 
 * @author Carlos Morais
 */
class AnalyzingTask extends ProcessSubtask {

	private Configuration configurationSnapshot;
	private ModuleResultDatabaseDao moduleResultDao;

	@Override
	protected void perform() {
		moduleResultDao = daoFactory().createModuleResultDao();
		configurationSnapshot = daoFactory().createConfigurationDao().snapshotFor(processing().getId());
		for (NativeModuleResult nativeModuleResult : resultProducer())
			analyzing(nativeModuleResult);
	}

	private void analyzing(NativeModuleResult nativeResult) {
		ModuleResult moduleResult = moduleResultDao.prepareResultFor(nativeResult.getModule(), processing().getId());
		addMetricResults(moduleResult, nativeResult.getMetricResults());
		configureAndSave(moduleResult);
	}

	private void addMetricResults(ModuleResult moduleResult, Collection<NativeMetricResult> metricResults) {
		for (NativeMetricResult metricResult : metricResults)
			addMetricResult(moduleResult, metricResult);
	}

	private void addMetricResult(ModuleResult moduleResult, NativeMetricResult nativeMetricResult) {
		Metric metric = nativeMetricResult.getMetric();
		Double value = nativeMetricResult.getValue();
		MetricConfiguration snapshot = configurationSnapshot.getConfigurationFor(metric);
		moduleResult.addMetricResult(new MetricResult(snapshot, value));
		addValueToAncestry(moduleResult, snapshot, value);
	}

	private void addValueToAncestry(ModuleResult moduleResult, MetricConfiguration snapshot, Double value) {
		ModuleResult ancestor = moduleResult;
		while (ancestor.hasParent()) {
			ancestor = ancestor.getParent();
			addDescendantResult(ancestor, snapshot, value);
		}
	}

	private void addDescendantResult(ModuleResult moduleResult, MetricConfiguration snapshot, Double descendantResult) {
		Metric metric = snapshot.getMetric();
		if (! moduleResult.hasResultFor(metric))
			moduleResult.addMetricResult(new MetricResult(snapshot, Double.NaN));
		moduleResult.getResultFor(metric).addDescendantResult(descendantResult);
	}

	private void configureAndSave(ModuleResult moduleResult) {
		ModuleResultConfigurer.configure(moduleResult, configurationSnapshot);
		save(moduleResult);
		if (moduleResult.hasParent())
			configureAndSave(moduleResult.getParent());
		else
			changeRootName(moduleResult);
	}

	private void changeRootName(ModuleResult resultsRoot) {
		if (! resultsRoot.getModule().getName()[0].equals(repository().getName())) {
			resultsRoot.getModule().getName()[0] = repository().getName();
			save(resultsRoot);
		}
		processing().setResultsRoot(resultsRoot);
	}

	private void save(ModuleResult moduleResult) {
		moduleResultDao.save(moduleResult, processing().getId());
	}
}