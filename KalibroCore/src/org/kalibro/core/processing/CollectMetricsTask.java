package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.core.MetricCollector;
import org.kalibro.dao.DaoFactory;

public class CollectMetricsTask extends ProcessProjectSubtask<Map<Module, ModuleResult>> {

	private Map<Module, ModuleResult> resultMap;

	protected CollectMetricsTask(ProjectResult projectResult) {
		super(projectResult);
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.COLLECTING;
	}

	@Override
	protected Map<Module, ModuleResult> compute() throws Exception {
		resultMap = new HashMap<Module, ModuleResult>();
		Configuration configuration = DaoFactory.getConfigurationDao().getConfigurationFor(project.getName());
		Map<String, Set<NativeMetric>> metricsMap = configuration.getNativeMetrics();
		for (String baseToolName : metricsMap.keySet())
			collectMetrics(baseToolName, metricsMap.get(baseToolName));
		return resultMap;
	}

	private void collectMetrics(String baseToolName, Set<NativeMetric> metrics) throws Exception {
		File codeDirectory = project.getDirectory();
		MetricCollector metricCollector = DaoFactory.getBaseToolDao().getBaseTool(baseToolName).createMetricCollector();
		Set<NativeModuleResult> nativeResults = metricCollector.collectMetrics(codeDirectory, metrics);
		for (NativeModuleResult nativeResult : nativeResults) {
			for (NativeMetricResult metricResult : nativeResult.getMetricResults())
				((NativeMetric) metricResult.getMetric()).setOrigin(baseToolName);
			putResult(nativeResult);
		}
	}

	private void putResult(NativeModuleResult nativeResult) {
		Module module = nativeResult.getModule();
		changeModuleNameIfRoot(module);
		if (!resultMap.containsKey(module))
			resultMap.put(module, new ModuleResult(module, projectResult.getDate()));
		resultMap.get(module).addMetricResults(nativeResult.getMetricResults());
	}

	private void changeModuleNameIfRoot(Module module) {
		if (module.getGranularity() == Granularity.SOFTWARE)
			module.setName(project.getName());
	}
}