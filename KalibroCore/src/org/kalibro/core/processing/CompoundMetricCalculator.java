package org.kalibro.core.processing;

import org.kalibro.*;

public class CompoundMetricCalculator {

	private ModuleResult moduleResult;
	private Configuration configuration;
	private JavascriptEvaluator scriptEvaluator;

	public CompoundMetricCalculator(ModuleResult moduleResult, Configuration configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
	}

	public void configure() {
		scriptEvaluator = new JavascriptEvaluator();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			include(metricConfiguration);
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics())
			if (isScopeCompatible(compoundMetric))
				computeCompoundMetric(configuration.getConfigurationFor(compoundMetric));
	}

	private void include(MetricConfiguration metricConfiguration) {
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
			CompoundMetric metric = (CompoundMetric) metricConfiguration.getMetric();
			moduleResult.addMetricResult(new MetricResult(metric, exception));
		}
	}

	private void doComputeCompoundMetric(MetricConfiguration metricConfiguration) {
		Double calculatedResult = scriptEvaluator.evaluate(metricConfiguration.getCode());
		moduleResult.addMetricResult(new MetricResult(metricConfiguration, calculatedResult));
	}
}