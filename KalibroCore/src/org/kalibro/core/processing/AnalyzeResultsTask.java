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
class AnalyzeResultsTask extends ProcessSubtask<Void> {

	private Configuration configurationSnapshot;
	private ModuleResultDatabaseDao moduleResultDao;
	private Producer<NativeModuleResult> resultProducer;

	AnalyzeResultsTask(Processing processing, Producer<NativeModuleResult> resultProducer) {
		super(processing);
		this.resultProducer = resultProducer;
		DatabaseDaoFactory daoFactory = new DatabaseDaoFactory();
		moduleResultDao = daoFactory.createModuleResultDao();
		configurationSnapshot = daoFactory.createConfigurationDao().snapshotFor(processing.getId());
	}

	@Override
	protected Void compute() {
		for (NativeModuleResult nativeModuleResult : resultProducer)
			new Analyzer(nativeModuleResult);
		return null;
	}

	private Module prepareModule(Module module) {
		if (module.getGranularity() == SOFTWARE)
			return new Module(SOFTWARE, processing.getRepository().getName());
		return module;
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

	@Override
	ProcessState getNextState() {
		return ProcessState.READY;
	}

	private final class Analyzer {

		private ModuleResult moduleResult;

		private Analyzer(NativeModuleResult nativeResult) {
			Module module = prepareModule(nativeResult.getModule());
			moduleResult = moduleResultDao.prepareResultFor(module, processing.getId());
			addMetricResults(nativeResult.getMetricResults());
			configureAndSave(moduleResult);
		}

		private void addMetricResults(Collection<NativeMetricResult> metricResults) {
			for (NativeMetricResult metricResult : metricResults)
				addMetricResult(metricResult);
		}

		private void addMetricResult(NativeMetricResult nativeMetricResult) {
			Metric metric = nativeMetricResult.getMetric();
			Double value = nativeMetricResult.getValue();
			MetricConfiguration snapshot = configurationSnapshot.getConfigurationFor(metric);
			moduleResult.addMetricResult(new MetricResult(snapshot, value));
			addValueToAncestry(snapshot, value);
		}

		private void addValueToAncestry(MetricConfiguration snapshot, Double value) {
			ModuleResult ancestor = moduleResult;
			while (ancestor.hasParent()) {
				ancestor = moduleResult.getParent();
				addDescendantResult(ancestor, snapshot, value);
			}
		}
	}
}