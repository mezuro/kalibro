package org.kalibro.core.processing;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.TypedTask;
import org.kalibro.core.model.*;
import org.kalibro.core.model.enums.Granularity;

class AnalyzeProjectTask extends TypedTask<Collection<ModuleResult>> {

	private ProjectResult projectResult;
	private Map<Module, ModuleResult> resultMap;

	protected AnalyzeProjectTask(ProjectResult projectResult) {
		this.projectResult = projectResult;
	}

	@Override
	public Collection<ModuleResult> performAndGetResult() throws Exception {
		resultMap = new HashMap<Module, ModuleResult>();
		collectResults();
		new SourceTreeBuilder(projectResult).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(projectResult, resultMap).aggregate();
		return resultMap.values();
	}

	private void collectResults() throws Exception {
		Map<String, Set<NativeMetric>> metricsMap = getMetricsMap();
		for (String baseToolName : metricsMap.keySet())
			collectResults(baseToolName, metricsMap.get(baseToolName));
	}

	private void collectResults(String baseToolName, Set<NativeMetric> metrics) throws Exception {
		Set<NativeModuleResult> collectedResults = collectMetrics(baseToolName, metrics);
		for (NativeModuleResult nativeResult : collectedResults) {
			for (NativeMetricResult metricResult : nativeResult.getMetricResults())
				((NativeMetric) metricResult.getMetric()).setOrigin(baseToolName);
			putResult(nativeResult);
		}
	}

	private Map<String, Set<NativeMetric>> getMetricsMap() {
		return Kalibro.getConfigurationDao().getConfigurationFor(getProjectName()).getNativeMetrics();
	}

	private Set<NativeModuleResult> collectMetrics(String baseToolName, Set<NativeMetric> metrics) throws Exception {
		BaseTool baseTool = Kalibro.getBaseToolDao().getBaseTool(baseToolName);
		return baseTool.createMetricCollector().collectMetrics(getCodeDirectory(), metrics);
	}

	private File getCodeDirectory() {
		return Kalibro.currentSettings().getLoadDirectoryFor(projectResult.getProject());
	}

	private void putResult(NativeModuleResult nativeResult) {
		Module module = nativeResult.getModule();
		changeModuleNameIfRoot(module);
		if (!resultMap.containsKey(module))
			resultMap.put(module, new ModuleResult(module, projectResult.getDate()));
		resultMap.get(module).addMetricResults(nativeResult.getMetricResults());
	}

	private void changeModuleNameIfRoot(Module module) {
		if (module.getGranularity() == Granularity.APPLICATION)
			module.setName(getProjectName());
	}

	private String getProjectName() {
		return projectResult.getProject().getName();
	}
}