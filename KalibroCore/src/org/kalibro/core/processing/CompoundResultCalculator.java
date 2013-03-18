package org.kalibro.core.processing;

import java.util.ArrayList;
import java.util.List;

import org.kalibro.*;

/**
 * Uses a {@link Configuration} to calculate compound metrics results for a {@link ModuleResult}.
 * 
 * @author Carlos Morais
 */
class CompoundResultCalculator {

	private ModuleResult moduleResult;
	private Configuration configuration;
	private JavascriptEvaluator scriptEvaluator;

	private List<MetricResult> compoundResults;

	CompoundResultCalculator(ModuleResult moduleResult, Configuration configuration) {
		this.moduleResult = moduleResult;
		this.configuration = configuration;
		this.scriptEvaluator = new JavascriptEvaluator();
	}

	List<MetricResult> calculateCompoundResults() {
		compoundResults = new ArrayList<MetricResult>();
		for (MetricConfiguration metricConfiguration : configuration.getMetricConfigurations())
			includeScriptFor(metricConfiguration);
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics())
			if (isScopeCompatible(compoundMetric))
				computeCompoundResult(configuration.getConfigurationFor(compoundMetric));
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

	private void computeCompoundResult(MetricConfiguration metricConfiguration) {
		try {
			Double calculatedResult = scriptEvaluator.evaluate(metricConfiguration.getCode());
			compoundResults.add(new MetricResult(metricConfiguration, calculatedResult));
		} catch (Exception exception) {
			compoundResults.add(new MetricResult(metricConfiguration, exception));
		}
	}
}