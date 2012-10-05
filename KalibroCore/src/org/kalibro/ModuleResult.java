package org.kalibro;

import java.util.*;

import org.kalibro.core.abstractentity.Ignore;

/**
 * Result of processing a {@link Module}. Represents a node in the source tree with the results associated.
 * 
 * @author Carlos Morais
 */
public class ModuleResult extends AbstractModuleResult<MetricResult> {

	@Ignore
	private ModuleResult parent;

	private Set<ModuleResult> children;

	public ModuleResult(ModuleResult parent, Module module) {
		super(module);
		this.parent = parent;
		setChildren(new TreeSet<ModuleResult>());
	}

	public ModuleResult getParent() {
		return parent;
	}

	public SortedSet<ModuleResult> getChildren() {
		return new TreeSet<ModuleResult>(children);
	}

	public void setChildren(SortedSet<ModuleResult> children) {
		this.children = children;
	}

	public Double getGrade() {
		double gradeSum = 0.0;
		double weightSum = 0.0;
		for (MetricResult metricResult : getMetricResults())
			if (metricResult.hasGrade()) {
				Double weight = metricResult.getWeight();
				gradeSum += metricResult.getGrade() * weight;
				weightSum += weight;
			}
		return gradeSum / weightSum;
	}

	public Map<CompoundMetric, Throwable> getCompoundMetricsWithError() {
		Map<CompoundMetric, Throwable> compoundMetricsWithError = new TreeMap<CompoundMetric, Throwable>();
		for (MetricResult metricResult : getMetricResults())
			if (metricResult.hasError())
				compoundMetricsWithError.put((CompoundMetric) metricResult.getMetric(), metricResult.getError());
		return compoundMetricsWithError;
	}
}