package org.kalibro.core.model;

import java.util.*;

import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;
import org.kalibro.core.processing.ModuleResultConfigurer;

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
		setGrade(null);
	}

	public void setConfiguration(Configuration configuration) {
		new ModuleResultConfigurer(this, configuration).configure();
	}

	public void addMetricResults(Collection<NativeMetricResult> nativeResults) {
		for (NativeMetricResult metricResult : nativeResults)
			addMetricResult(new MetricResult(metricResult));
	}

	public void removeCompoundMetrics() {
		compoundMetricsWithError.clear();
		for (Metric metric : metricResults.keySet())
			if (metric.isCompound())
				removeResultFor(metric);
	}

	public void removeResultFor(Metric metric) {
		metricResults.remove(metric);
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