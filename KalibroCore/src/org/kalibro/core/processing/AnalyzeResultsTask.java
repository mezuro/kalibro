package org.kalibro.core.processing;

import static org.kalibro.Granularity.SOFTWARE;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;

class AnalyzeResultsTask extends ProcessSubtask<Void> {

	private Producer<NativeModuleResult> resultProducer;

	AnalyzeResultsTask(Processing processing, Producer<NativeModuleResult> resultProducer) {
		super(processing);
		this.resultProducer = resultProducer;
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.READY;
	}

	@Override
	protected Void compute() {
		new SourceTreeBuilder(processing).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(processing, resultMap).aggregate();
		return null;
	}

	private void changeModuleNameIfRoot(Module module) {
		if (module.getGranularity() == Granularity.SOFTWARE)
			module.setName(project.getName());
	}
}

class ResultsAggregator {

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

class SourceTreeBuilder {

	private ModuleNode sourceRoot;
	private RepositoryResult repositoryResult;

	protected SourceTreeBuilder(RepositoryResult repositoryResult) {
		this.repositoryResult = repositoryResult;
	}

	protected void buildSourceTree(Collection<Module> modules) {
		String projectName = repositoryResult.getRepository().getName();
		sourceRoot = new ModuleNode(new Module(SOFTWARE, projectName));
		for (Module module : modules)
			addModule(module);
		repositoryResult.setResultsRoot(sourceRoot);
	}

	private void addModule(Module module) {
		if (module.getGranularity() != SOFTWARE) {
			ModuleNode parent = addInferredAncestry(module);
			if (parent.hasChildFor(module))
				parent.getChildFor(module).setModule(module);
			else
				parent.addChild(new ModuleNode(module));
		}
	}

	private ModuleNode addInferredAncestry(Module module) {
		ModuleNode parent = sourceRoot;
		for (Module ancestor : module.inferAncestry()) {
			ModuleNode child;
			if (parent.hasChildFor(ancestor))
				child = parent.getChildFor(ancestor);
			else {
				child = new ModuleNode(ancestor);
				parent.addChild(child);
			}
			parent = child;
		}
		return parent;
	}
}