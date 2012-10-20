package org.kalibro.core.processing;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.Writer;

class CollectMetricsTask extends ProcessSubtask<Producer<NativeModuleResult>> {

	private File codeDirectory;

	CollectMetricsTask(Processing processing, File codeDirectory) {
		super(processing);
		this.codeDirectory = codeDirectory;
	}

	@Override
	protected Producer<NativeModuleResult> compute() throws Exception {
		Producer<NativeModuleResult> resultProducer = new Producer<NativeModuleResult>();
		Configuration configuration = processing.getRepository().getConfiguration();
		Map<BaseTool, Set<NativeMetric>> metricsMap = configuration.getNativeMetrics();
		for (BaseTool baseTool : metricsMap.keySet())
			collectMetrics(baseTool, metricsMap.get(baseTool), resultProducer.createWriter());
		return resultProducer;
	}

	private void collectMetrics(
		BaseTool baseTool, Set<NativeMetric> metrics, Writer<NativeModuleResult> resultsWriter) throws Exception {
		// TODO in background
		baseTool.collectMetrics(codeDirectory, metrics, resultsWriter);
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.ANALYZING;
	}
}