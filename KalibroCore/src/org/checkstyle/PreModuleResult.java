package org.checkstyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;

public class PreModuleResult {

	private Module module;
	private Map<String, PreMetricResult> metricResults;

	public PreModuleResult(Module module, Set<NativeMetric> wantedMetrics) {
		this.module = module;
		initializeMetricResults(wantedMetrics);
	}

	private void initializeMetricResults(Set<NativeMetric> wantedMetrics) {
		metricResults = new HashMap<String, PreMetricResult>();
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			if (wantedMetrics.contains(metric.getNativeMetric()))
				metricResults.put(metric.getMessageKey(), new PreMetricResult(metric));
	}

	public void addMetricResult(String messageKey, Double value) {
		if (metricResults.containsKey(messageKey))
			metricResults.get(messageKey).addValue(value);
	}

	public NativeModuleResult getModuleResult() {
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (PreMetricResult metricResult : metricResults.values())
			moduleResult.addMetricResult(metricResult.getResult());
		return moduleResult;
	}
}