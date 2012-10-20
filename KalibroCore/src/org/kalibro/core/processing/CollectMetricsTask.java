package org.kalibro.core.processing;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.dao.DaoFactory;

class CollectMetricsTask extends ProcessSubtask<Producer<NativeModuleResult>> {

	private Map<Module, NativeModuleResult> results;
	private File codeDirectory;

	CollectMetricsTask(Processing processing, File codeDirectory) {
		super(processing);
		this.codeDirectory = codeDirectory;
	}

	@Override
	protected Producer<NativeModuleResult> compute() throws Exception {
		results = new HashMap<Module, ModuleResult>();
		Configuration configuration = DaoFactory.getConfigurationDao().configurationOf(project.getId());
		Map<BaseTool, Set<NativeMetric>> metricsMap = configuration.getNativeMetrics();
		for (BaseTool baseTool : metricsMap.keySet())
			collectMetrics(baseTool, metricsMap.get(baseTool));
		return new HashSet<NativeModuleResult>(results.values());
	}

	private void collectMetrics(BaseTool baseTool, Set<NativeMetric> metrics) throws Exception {
		Set<NativeModuleResult> nativeResults = baseTool.collectMetrics(codeDirectory, metrics);
		for (NativeModuleResult nativeResult : nativeResults)
			putResult(nativeResult);
	}

	private void putResult(NativeModuleResult nativeResult) {
		Module module = nativeResult.getModule();
		changeModuleNameIfRoot(module);
		if (!results.containsKey(module))
			results.put(module, new ModuleResult(module, processing.getDate()));
		results.get(module).addMetricResults(nativeResult.getMetricResults());
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.ANALYZING;
	}
}