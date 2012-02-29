package org.checkstyle;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeMetric;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

public class PreModuleResult {

	private Module module;
	private Set<NativeMetric> wantedMetrics;
	private Map<String, PreMetricResult> metricResults;

	public PreModuleResult(String className, Set<NativeMetric> wantedMetrics) {
		module = new Module(Granularity.CLASS, className);
		this.wantedMetrics = wantedMetrics;
		initializeMetricResults();
	}

	private void initializeMetricResults() {
		metricResults = new HashMap<String, PreMetricResult>();
		for (CheckstyleMetric metric : CheckstyleMetric.values())
			if (wantedMetrics.contains(metric.getNativeMetric()))
				metricResults.put(metric.getMessageKey(), new PreMetricResult(metric));
	}

	public void addMetricResult(String messageKey, Double value) {
		metricResults.get(messageKey).addValue(value);
	}

	public NativeModuleResult getModuleResult() {
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (PreMetricResult metricResult : metricResults.values())
			moduleResult.addMetricResult(metricResult.getResult());
		return moduleResult;
	}
}