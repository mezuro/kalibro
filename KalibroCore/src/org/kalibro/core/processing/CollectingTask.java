package org.kalibro.core.processing;

import java.util.Map;
import java.util.Set;

import org.kalibro.BaseTool;
import org.kalibro.Configuration;
import org.kalibro.NativeMetric;
import org.kalibro.Repository;

/**
 * Collects metric results using the {@link BaseTool}s specified at the {@link Repository}'s {@link Configuration}.
 * 
 * @author Carlos Morais
 */
class CollectingTask extends ProcessSubtask {

	@Override
	protected void perform() throws Exception {
		Configuration configuration = repository().getConfiguration();
		Map<BaseTool, Set<NativeMetric>> wantedMetrics = configuration.getNativeMetrics();
		for (BaseTool baseTool : wantedMetrics.keySet())
			baseTool.collectMetrics(codeDirectory(), wantedMetrics.get(baseTool), resultProducer().createWriter());
	}
}