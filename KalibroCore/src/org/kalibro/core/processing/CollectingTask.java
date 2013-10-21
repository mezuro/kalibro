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
class CollectingTask extends ProcessSubtask {

	CollectingTask(ProcessContext context) {
		super(context);
	}

	@Override
	protected void perform() throws Exception {
		File codeDirectory = context.codeDirectory;
		Producer<NativeModuleResult> resultProducer = context.resultProducer;
		Map<BaseTool, Set<NativeMetric>> wantedMetrics = context.configuration.getNativeMetrics();
		for (BaseTool baseTool : wantedMetrics.keySet())
			baseTool.collectMetrics(codeDirectory, wantedMetrics.get(baseTool), resultProducer.createWriter());
	}
}