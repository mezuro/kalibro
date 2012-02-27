package org.checkstyle;

import java.util.HashMap;
import java.util.Map;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.NativeModuleResult;
import org.kalibro.core.model.enums.Granularity;

public class PreModuleResult {

	private Module module;
	private Map<String, PreMetricResult> metricResults;

	public PreModuleResult(String className) {
		module = new Module(Granularity.CLASS, className);
		metricResults = new HashMap<String, PreMetricResult>();
	}

	public void addMetricResult(String messageKey, Double value) {
		if (!metricResults.containsKey(messageKey))
			metricResults.put(messageKey, new PreMetricResult(messageKey));
		metricResults.get(messageKey).addValue(value);
	}

	public NativeModuleResult getModuleResult() {
		NativeModuleResult moduleResult = new NativeModuleResult(module);
		for (PreMetricResult metricResult : metricResults.values())
			moduleResult.addMetricResult(metricResult.getResult());
		return moduleResult;
	}
}