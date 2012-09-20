package org.kalibro.core.processing;

import org.kalibro.Configuration;
import org.kalibro.core.model.*;

public class ModuleResultConfigurer {

	private ModuleResult moduleResult;
	private Configuration configuration;

	private Double gradeSum, weightSum;
	private ScriptEvaluator compoundEvaluator;

	public ModuleResultConfigurer(ModuleResult moduleResult, Configuration configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
	}

	public void configure() {
		clearConfiguration();
		configureNativeResults();
		computeCompoundMetrics();
		moduleResult.setGrade(gradeSum / weightSum);
	}

	private void clearConfiguration() {
		gradeSum = 0.0;
		weightSum = 0.0;
		compoundEvaluator = JavascriptEvaluator.create();
		moduleResult.removeCompoundMetrics();
	}

	private void configureNativeResults() {
		for (MetricResult metricResult : moduleResult.getMetricResults()) {
			String metricName = metricResult.getMetric().getName();
			if (configuration.containsMetric(metricName)) {
				MetricConfiguration metricConfiguration = configuration.getConfigurationFor(metricName);
				metricResult.setConfiguration(metricConfiguration);
				include(metricConfiguration);
				computeGrade(metricResult);
			}
		}
	}

	private void computeCompoundMetrics() {
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics())
			if (isScopeCompatible(compoundMetric))
				computeCompoundMetric(configuration.getConfigurationFor(compoundMetric.getName()));
	}

	private boolean isScopeCompatible(Metric metric) {
		return metric.getScope().ordinal() >= moduleResult.getModule().getGranularity().ordinal();
	}

	private void computeCompoundMetric(MetricConfiguration metricConfiguration) {
		try {
			doComputeCompoundMetric(metricConfiguration);
		} catch (Exception exception) {
			CompoundMetric metric = (CompoundMetric) metricConfiguration.getMetric();
			moduleResult.addCompoundMetricWithError(metric, exception);
		}
	}

	private void doComputeCompoundMetric(MetricConfiguration metricConfiguration) {
		include(metricConfiguration);
		Double calculatedResult = compoundEvaluator.evaluate(metricConfiguration.getCode());
		MetricResult metricResult = new MetricResult(metricConfiguration.getMetric(), calculatedResult);
		metricResult.setConfiguration(metricConfiguration);
		computeGrade(metricResult);
		moduleResult.addMetricResult(metricResult);
	}

	private void include(MetricConfiguration metricConfiguration) {
		String code = metricConfiguration.getCode();
		Metric metric = metricConfiguration.getMetric();
		if (metric.isCompound())
			compoundEvaluator.addFunction(code, ((CompoundMetric) metric).getScript());
		else
			compoundEvaluator.addVariable(code, moduleResult.getResultFor(metric).getValue());
	}

	private void computeGrade(MetricResult metricResult) {
		if (metricResult.hasRange()) {
			Double weight = configuration.getConfigurationFor(metricResult.getMetric().getName()).getWeight();
			gradeSum += metricResult.getGrade() * weight;
			weightSum += weight;
		}
	}
}