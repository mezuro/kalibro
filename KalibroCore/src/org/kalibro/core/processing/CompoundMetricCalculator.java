package org.kalibro.core.processing;

import org.kalibro.*;

public final class CompoundMetricCalculator {

	public static void addCompoundMetrics(ModuleResult moduleResult, Configuration configuration) {
		new CompoundMetricCalculator(moduleResult, configuration).addCompoundMetrics();
	}

	private ModuleResult moduleResult;
	private Configuration configuration;
	private JavascriptEvaluator scriptEvaluator;

	private CompoundMetricCalculator(ModuleResult moduleResult, Configuration configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
		this.scriptEvaluator = new JavascriptEvaluator();
	}

	private void addCompoundMetrics() {
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			includeScriptFor(metricConfiguration);
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics())
			if (isScopeCompatible(compoundMetric))
				computeCompoundMetric(configuration.getConfigurationFor(compoundMetric));
	}

	private void includeScriptFor(MetricConfiguration metricConfiguration) {
		String code = metricConfiguration.getCode();
		Metric metric = metricConfiguration.getMetric();
		if (metric.isCompound())
			scriptEvaluator.addFunction(code, ((CompoundMetric) metric).getScript());
		else
			scriptEvaluator.addVariable(code, moduleResult.getResultFor(metric).getValue());
	}

	private boolean isScopeCompatible(Metric metric) {
		return metric.getScope().ordinal() >= moduleResult.getModule().getGranularity().ordinal();
	}

	private void computeCompoundMetric(MetricConfiguration metricConfiguration) {
		try {
			doComputeCompoundMetric(metricConfiguration);
		} catch (Exception exception) {
			moduleResult.addMetricResult(new MetricResult(metricConfiguration, exception));
		}
	}

	private void doComputeCompoundMetric(MetricConfiguration metricConfiguration) {
		Double calculatedResult = scriptEvaluator.evaluate(metricConfiguration.getCode());
		moduleResult.addMetricResult(new MetricResult(metricConfiguration, calculatedResult));
	}
}