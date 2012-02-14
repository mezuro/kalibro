package org.kalibro.core.model;

import java.util.Set;
import java.util.TreeSet;

import org.kalibro.core.model.abstracts.AbstractEntity;
import org.kalibro.core.model.abstracts.IdentityField;
import org.kalibro.core.model.abstracts.SortingMethods;

@SortingMethods("getModule")
public class ModuleNode extends AbstractEntity<ModuleNode> {

	@IdentityField
	private Module module;

	private Set<ModuleNode> children;

	public ModuleNode(Module module) {
		setModule(module);
		children = new TreeSet<ModuleNode>();
	}

	@Override
	public String toString() {
		return module.toString();
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public Set<ModuleNode> getChildren() {
		return children;
	}

	public boolean hasChildFor(Module childModule) {
		try {
			getChildFor(childModule);
			return true;
		} catch (IllegalArgumentException exception) {
			return false;
		}
	}

	public ModuleNode getChildFor(Module childModule) {
		for (ModuleNode child : children)
			if (child.getModule().equals(childModule))
				return child;
		throw new IllegalArgumentException("Module " + module.getName() + " has no child named " + childModule);
	}

	public void addChild(ModuleNode child) {
		children.add(child);
	}
}