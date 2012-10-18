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

	public void addChild(ModuleResult child) {
		child.parent = this;
		children.add(child);
	}

	@Override
	public String toString() {
		return getModule().toString();
	}
}