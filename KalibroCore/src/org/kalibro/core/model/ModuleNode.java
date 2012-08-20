package org.kalibro.core.model;

import java.util.Set;
import java.util.TreeSet;

import org.kalibro.KalibroException;
import org.kalibro.core.abstractentity.AbstractEntity;
import org.kalibro.core.abstractentity.IdentityField;
import org.kalibro.core.abstractentity.SortingMethods;

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
		return findChildFor(childModule) != null;
	}

	public ModuleNode getChildFor(Module childModule) {
		ModuleNode child = findChildFor(childModule);
		if (child == null)
			throw new KalibroException("Module " + module.getName() + " has no child named " + childModule);
		return child;
	}

	private ModuleNode findChildFor(Module childModule) {
		for (ModuleNode child : children)
			if (child.getModule().equals(childModule))
				return child;
		return null;
	}

	public void addChild(ModuleNode child) {
		children.add(child);
	}
}