package org.kalibro.core.processing;

import java.io.File;
import java.util.Map;
import java.util.Set;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;

/**
 * Collects metric results using the {@link BaseTool}s specified at the {@link Repository}'s {@link Configuration}.
 * 
 * @author Carlos Morais
 */
class CollectMetricsTask extends ProcessSubtask<Void> {

	private File codeDirectory;
	private Producer<NativeModuleResult> resultProducer;

	CollectMetricsTask(Processing processing, File codeDirectory, Producer<NativeModuleResult> resultProducer) {
		super(processing);
		this.codeDirectory = codeDirectory;
		this.resultProducer = resultProducer;
	}

	@Override
	protected Void compute() throws Exception {
		Configuration configuration = processing.getRepository().getConfiguration();
		Map<BaseTool, Set<NativeMetric>> wantedMetrics = configuration.getNativeMetrics();
		for (BaseTool baseTool : wantedMetrics.keySet())
			baseTool.collectMetrics(codeDirectory, wantedMetrics.get(baseTool), resultProducer.createWriter());
		return null;
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.ANALYZING;
	}
}