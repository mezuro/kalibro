package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.dao.DaoFactory;

public class CollectMetricsTask extends ProcessProjectSubtask<Map<Module, ModuleResult>> {

	private Map<Module, ModuleResult> resultMap;

	protected CollectMetricsTask(RepositoryResult repositoryResult) {
		super(repositoryResult);
	}

	@Override
	protected RepositoryState getTaskState() {
		return RepositoryState.COLLECTING;
	}

	@Override
	protected Map<Module, ModuleResult> compute() throws Exception {
		resultMap = new HashMap<Module, ModuleResult>();
		Configuration configuration = DaoFactory.getConfigurationDao().configurationOf(project.getId());
		Map<BaseTool, Set<NativeMetric>> metricsMap = configuration.getNativeMetrics();
		for (BaseTool baseTool : metricsMap.keySet())
			collectMetrics(baseTool, metricsMap.get(baseTool));
		return resultMap;
	}

	private void collectMetrics(BaseTool baseTool, Set<NativeMetric> metrics) throws Exception {
		File codeDirectory = project.getDirectory();
		Set<NativeModuleResult> nativeResults = baseTool.collectMetrics(codeDirectory, metrics);
		for (NativeModuleResult nativeResult : nativeResults)
			putResult(nativeResult);
	}

	private void putResult(NativeModuleResult nativeResult) {
		Module module = nativeResult.getModule();
		changeModuleNameIfRoot(module);
		if (!resultMap.containsKey(module))
			resultMap.put(module, new ModuleResult(module, repositoryResult.getDate()));
		resultMap.get(module).addMetricResults(nativeResult.getMetricResults());
	}

	private void changeModuleNameIfRoot(Module module) {
		if (module.getGranularity() == Granularity.SOFTWARE)
			module.setName(project.getName());
	}
}