package org.kalibro.core.processing;

import java.util.Date;
import java.util.Map;

import org.kalibro.*;

public class ResultsAggregator {

	private Date date;
	private ModuleNode node;
	private Map<Module, ModuleResult> resultMap;

	public ResultsAggregator(RepositoryResult repositoryResult, Map<Module, ModuleResult> resultMap) {
		this(repositoryResult.getDate(), repositoryResult.getResultsRoot(), resultMap);
	}

	private ResultsAggregator(Date date, ModuleNode node, Map<Module, ModuleResult> resultMap) {
		this.date = date;
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
			resultMap.put(module, new ModuleResult(module, date));
		return resultMap.get(module);
	}
}