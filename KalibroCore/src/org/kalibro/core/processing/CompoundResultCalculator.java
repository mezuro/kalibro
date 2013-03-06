package org.kalibro.core.processing;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.*;

/**
 * Uses a {@link Configuration} to calculate compound metrics and grade of a {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
class CompoundResultCalculator {

	private ModuleResult moduleResult;
	private Configuration configuration;
	private JavascriptEvaluator scriptEvaluator;

	CompoundResultCalculator(ModuleResult moduleResult, Configuration configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
		this.scriptEvaluator = new JavascriptEvaluator();
	}

	List<MetricResult> calculateCompoundMetricResults() {
		List<MetricResult> compoundResults = new ArrayList<MetricResult>();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			includeScriptFor(metricConfiguration);
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics())
			if (isScopeCompatible(compoundMetric))
				compoundResults.add(computeCompoundResult(configuration.getConfigurationFor(compoundMetric)));
		scriptEvaluator.close();
		return compoundResults;
	}

	private void includeScriptFor(MetricConfiguration metricConfiguration) {
		String code = metricConfiguration.getCode();
		Metric metric = metricConfiguration.getMetric();
		if (!isScopeCompatible(metric))
			return;
		if (metric.isCompound())
			scriptEvaluator.addFunction(code, ((CompoundMetric) metric).getScript());
		else if (moduleResult.hasResultFor(metric))
			scriptEvaluator.addVariable(code, moduleResult.getResultFor(metric).getAggregatedValue());
	}

	private boolean isScopeCompatible(Metric metric) {
		return metric.getScope().ordinal() >= moduleResult.getModule().getGranularity().ordinal();
	}

	private MetricResult computeCompoundResult(MetricConfiguration metricConfiguration) {
		MetricResult compoundResult;
		try {
			compoundResult = doComputeCompoundResult(metricConfiguration);
		} catch (Exception exception) {
			compoundResult = new MetricResult(metricConfiguration, exception);
		}
		moduleResult.addMetricResult(compoundResult);
		return compoundResult;
	}

	private MetricResult doComputeCompoundResult(MetricConfiguration metricConfiguration) {
		Double calculatedResult = scriptEvaluator.evaluate(metricConfiguration.getCode());
		return new MetricResult(metricConfiguration, calculatedResult);
	}

	Double calculateGrade() {
		double gradeSum = 0.0;
		double weightSum = 0.0;
		for (MetricResult metricResult : moduleResult.getMetricResults())
			if (metricResult.hasGrade()) {
				Double weight = metricResult.getWeight();
				gradeSum += metricResult.getGrade() * weight;
				weightSum += weight;
			}
		return gradeSum / weightSum;
	}
}