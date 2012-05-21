package org.kalibro.core.model;

import java.util.*;

import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.processing.ScriptBuilder;
import org.kalibro.core.processing.ScriptEvaluator;

@SortingMethods({"getDate", "getModule"})
public class ModuleResult extends AbstractModuleResult<MetricResult> {

	@IdentityField
	private Date date;

	private Double grade;
	private Map<CompoundMetric, Throwable> compoundMetricsWithError;

	public ModuleResult(Module module, Date date) {
		super(module);
		this.date = date;
		this.compoundMetricsWithError = new TreeMap<CompoundMetric, Throwable>();
	}

	public void setConfiguration(Configuration configuration) {
		removeCompoundMetrics();
		setConfigurationOnResults(configuration);
		computeCompoundMetrics(configuration);
		computeGrade(configuration);
	}

	private void removeCompoundMetrics() {
		compoundMetricsWithError.clear();
		for (Metric metric : new HashSet<Metric>(metricResults.keySet()))
			if (metric.isCompound())
				metricResults.remove(metric);
	}

	private void setConfigurationOnResults(Configuration configuration) {
		for (Metric metric : metricResults.keySet()) {
			String metricName = metric.getName();
			MetricResult metricResult = metricResults.get(metric);
			if (configuration.containsMetric(metricName))
				metricResult.setConfiguration(configuration.getConfigurationFor(metricName));
		}
	}

	private void computeCompoundMetrics(Configuration configuration) {
		for (CompoundMetric compoundMetric : configuration.getCompoundMetrics()) {
			ScriptBuilder scriptBuilder = new ScriptBuilder(configuration, this, compoundMetric);
			ScriptEvaluator scriptEvaluator = new ScriptEvaluator(scriptBuilder.buildScript());
			if (scriptBuilder.shouldInclude(compoundMetric))
				includeCompoundMetric(configuration.getConfigurationFor(compoundMetric.getName()), scriptEvaluator);
		}
	}

	private void includeCompoundMetric(MetricConfiguration configuration, ScriptEvaluator scriptEvaluator) {
		CompoundMetric compoundMetric = (CompoundMetric) configuration.getMetric();
		try {
			Double calculatedResult = scriptEvaluator.invokeFunction(configuration.getCode());
			MetricResult metricResult = new MetricResult(compoundMetric, calculatedResult);
			metricResult.setConfiguration(configuration);
			metricResults.put(compoundMetric, metricResult);
		} catch (Exception exception) {
			addCompoundMetricWithError(compoundMetric, exception);
		}
	}

	private void computeGrade(Configuration configuration) {
		Double gradeSum = 0.0, weightSum = 0.0;
		for (Metric metric : metricResults.keySet()) {
			MetricResult metricResult = metricResults.get(metric);
			if (metricResult.hasRange()) {
				Double weight = configuration.getConfigurationFor(metric.getName()).getWeight();
				gradeSum += metricResult.getGrade() * weight;
				weightSum += weight;
			}
		}
		setGrade(gradeSum / weightSum);
	}

	public Date getDate() {
		return date;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public void addMetricResults(Collection<NativeMetricResult> nativeResults) {
		for (NativeMetricResult metricResult : nativeResults)
			addMetricResult(new MetricResult(metricResult));
	}

	public Set<CompoundMetric> getCompoundMetricsWithError() {
		return compoundMetricsWithError.keySet();
	}

	public Throwable getErrorFor(CompoundMetric metric) {
		return compoundMetricsWithError.get(metric);
	}

	public void addCompoundMetricWithError(CompoundMetric metric, Throwable error) {
		compoundMetricsWithError.put(metric, error);
	}
}