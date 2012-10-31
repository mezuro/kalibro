package org.kalibro.core.processing;

import static org.kalibro.Granularity.SOFTWARE;

import java.util.Collection;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

/**
 * Analyzes and saves metric results collected by {@link BaseTool}s.
 * 
 * @author Carlos Morais
 */
class AnalyzingTask extends ProcessSubtask<Void> {

	private Configuration configurationSnapshot;
	private ModuleResultDatabaseDao moduleResultDao;
	private Producer<NativeModuleResult> resultProducer;

	AnalyzingTask(Processing processing, Producer<NativeModuleResult> resultProducer) {
		super(processing);
		this.resultProducer = resultProducer;
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		moduleResultDao = daoFactory.createModuleResultDao();
		configurationSnapshot = daoFactory.createConfigurationDao().snapshotFor(processing.getId());
	}

	@Override
	protected Void compute() {
		for (NativeModuleResult nativeModuleResult : resultProducer)
			analyzing(nativeModuleResult);
		return null;
	}

	private void analyzing(NativeModuleResult nativeResult) {
		Module module = prepareModule(nativeResult.getModule());
		ModuleResult moduleResult = moduleResultDao.prepareResultFor(module, processing.getId());
		addMetricResults(moduleResult, nativeResult.getMetricResults());
		configureAndSave(moduleResult);
	}

	private Module prepareModule(Module module) {
		if (module.getGranularity() == SOFTWARE)
			return new Module(SOFTWARE, processing.getRepository().getName());
		return module;
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
			ancestor = moduleResult.getParent();
			addDescendantResult(ancestor, snapshot, value);
		}
	}

	private void addDescendantResult(ModuleResult moduleResult, MetricConfiguration snapshot, Double descendantResult) {
		Metric metric = snapshot.getMetric();
		if (!moduleResult.hasResultFor(metric))
			moduleResult.addMetricResult(new MetricResult(snapshot, Double.NaN));
		moduleResult.getResultFor(metric).addDescendantResult(descendantResult);
	}

	private void configureAndSave(ModuleResult moduleResult) {
		ModuleResultConfigurer.configure(moduleResult, configurationSnapshot);
		moduleResultDao.save(moduleResult, processing.getId());
		if (moduleResult.hasParent())
			configureAndSave(moduleResult.getParent());
	}
}