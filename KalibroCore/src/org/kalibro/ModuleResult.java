package org.kalibro;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.kalibro.core.abstractentity.Ignore;

/**
 * Result of processing a {@link Module}. Represents a node in the source tree with the results associated.
 * 
 * @author Carlos Morais
 */
public class ModuleResult extends AbstractModuleResult<MetricResult> {

	private Double grade;

	@Ignore
	private ModuleResult parent;
	private Set<ModuleResult> children;

	public ModuleResult(ModuleResult parent, Module module) {
		super(module);
		this.grade = Double.NaN;
		this.parent = parent;
		setChildren(new TreeSet<ModuleResult>());
	}

	public void calculateGrade() {
		double gradeSum = 0.0;
		double weightSum = 0.0;
		for (MetricResult metricResult : getMetricResults())
			if (metricResult.hasGrade()) {
				Double weight = metricResult.getWeight();
				gradeSum += metricResult.getGrade() * weight;
				weightSum += weight;
			}
		grade = gradeSum / weightSum;
	}

	public Double getGrade() {
		return grade;
	}

	public ModuleResult getParent() {
		return parent;
	}

	public SortedSet<ModuleResult> getChildren() {
		for (ModuleResult child : children)
			child.parent = this;
		return new TreeSet<ModuleResult>(children);
	}

	public void setChildren(SortedSet<ModuleResult> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return getModule().toString();
	}
}