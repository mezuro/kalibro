package org.kalibro.core.processing;

import static org.kalibro.Granularity.SOFTWARE;

import java.util.Collection;
import java.util.Map;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;

class AnalyzeResultsTask extends ProcessSubtask<Void> {

	private ModuleResultDatabaseDao moduleResultDao;
	private Producer<NativeModuleResult> resultProducer;

	AnalyzeResultsTask(Processing processing, Producer<NativeModuleResult> resultProducer) {
		super(processing);
		this.resultProducer = resultProducer;
		this.moduleResultDao = new DatabaseDaoFactory().createModuleResultDao();
	}

	@Override
	protected Void compute() {
		for (NativeModuleResult nativeModuleResult : resultProducer)
			analyze(nativeModuleResult);
		return null;
	}

	private void analyze(NativeModuleResult nativeModuleResult) {
		Module module = prepareModule(nativeModuleResult.getModule());
		ModuleResult moduleResult = moduleResultDao.prepareResultFor(module, processing.getId());
		addMetricResults(moduleResult, nativeModuleResult.getMetricResults());
		configureAndSave(moduleResult);
	}

	private Module prepareModule(Module module) {
		if (module.getGranularity() == SOFTWARE)
			return new Module(SOFTWARE, processing.getRepository().getName());
		return module;
	}

	private void addMetricResults(ModuleResult moduleResult, Collection<NativeMetricResult> metricResults) {
		// TODO add metric results
		// TODO for each metric result traverse ancestry adding descendant results
	}

	private void configureAndSave(ModuleResult moduleResult) {
		// TODO configure ModuleResult (grade and compound metrics)
		moduleResultDao.save(moduleResult, processing.getId());
		if (moduleResult.hasParent())
			configureAndSave(moduleResult.getParent());
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.READY;
	}
}

class ResultsAggregator {

	private ModuleNode node;
	private Map<Module, ModuleResult> resultMap;

	public ResultsAggregator(RepositoryResult repositoryResult, Map<Module, ModuleResult> resultMap) {
		this(repositoryResult.getDate(), repositoryResult.getResultsRoot(), resultMap);
	}

	private ResultsAggregator(ModuleNode node, Map<Module, ModuleResult> resultMap) {
		this.node = node;
		this.resultMap = resultMap;
	}

	public void aggregate() {
		for (ModuleNode child : node.getChildren()) {
			new ResultsAggregator(date, child, resultMap).aggregate();
			adddescendantResultsFrom(child);
		}
	}

	private void adddescendantResultsFrom(ModuleNode child) {
		ModuleResult childResult = resultMap.get(child.getModule());
		for (MetricResult metricResult : childResult.getMetricResults()) {
			Metric metric = metricResult.getMetric();
			if (!metric.isCompound())
				adddescendantResultsFrom(childResult, (NativeMetric) metric);
		}
	}

	private void adddescendantResultsFrom(ModuleResult childResult, NativeMetric metric) {
		MetricResult myMetricResult = prepareResultFor(metric);
		MetricResult childMetricResult = childResult.getResultFor(metric);
		if (!childMetricResult.getValue().isNaN())
			myMetricResult.adddescendantResult(childMetricResult.getValue());
		myMetricResult.adddescendantResults(childMetricResult.getdescendantResults());
	}

	private MetricResult prepareResultFor(NativeMetric metric) {
		ModuleResult moduleResult = prepareModuleResult();
		if (!moduleResult.hasResultFor(metric))
			moduleResult.addMetricResult(new MetricResult(metric, Double.NaN));
		return moduleResult.getResultFor(metric);
	}

	private ModuleResult prepareModuleResult() {
		Module module = node.getModule();
		if (!resultMap.containsKey(module))
			resultMap.put(module, new ModuleResult(module));
		return resultMap.get(module);
	}
}