package org.kalibro;

import java.util.*;

import org.kalibro.core.abstractentity.Ignore;
import org.kalibro.dao.DaoFactory;

/**
 * Result of processing a {@link Module}. Represents a node in the source tree with the results associated.
 * 
 * @author Carlos Morais
 */
public class ModuleResult extends AbstractModuleResult<MetricResult> {

	private Long id;

	private Double grade;

	@Ignore
	private ModuleResult parent;
	private Set<ModuleResult> children;

	/** Should NOT be used. Only for CGLIB proxy creation. */
	protected ModuleResult() {
		this(null, null);
	}

	public ModuleResult(ModuleResult parent, Module module) {
		super(module);
		setGrade(Double.NaN);
		this.parent = parent;
		setChildren(new TreeSet<ModuleResult>());
	}

	public Long getId() {
		return id;
	}

	public Double getGrade() {
		return grade;
	}

	public void setGrade(Double grade) {
		this.grade = grade;
	}

	public boolean hasParent() {
		return parent != null;
	}

	public ModuleResult getParent() {
		return parent;
	}

	public SortedSet<ModuleResult> getChildren() {
		TreeSet<ModuleResult> myChildren = new TreeSet<ModuleResult>();
		for (ModuleResult child : children) {
			child.parent = this;
			myChildren.add(child);
		}
		return myChildren;
	}

	public void setChildren(SortedSet<ModuleResult> children) {
		this.children = children;
	}

	public void addChild(ModuleResult child) {
		child.parent = this;
		children.add(child);
	}

	public SortedMap<Date, ModuleResult> history() {
		return DaoFactory.getModuleResultDao().historyOf(id);
	}

	public SortedMap<Date, MetricResult> historyOf(Metric metric) {
		return DaoFactory.getMetricResultDao().historyOf(metric.getName(), id);
	}

	@Override
	public String toString() {
		return getModule().toString();
	}
}