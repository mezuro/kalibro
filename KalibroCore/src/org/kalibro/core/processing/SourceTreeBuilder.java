package org.kalibro.core.processing;

import static org.kalibro.Granularity.SOFTWARE;

import java.util.Collection;

import org.kalibro.Module;
import org.kalibro.ModuleNode;
import org.kalibro.ProjectResult;

class SourceTreeBuilder {

	private ModuleNode sourceRoot;
	private ProjectResult projectResult;

	protected SourceTreeBuilder(ProjectResult projectResult) {
		this.projectResult = projectResult;
	}

	protected void buildSourceTree(Collection<Module> modules) {
		String projectName = projectResult.getProject().getName();
		sourceRoot = new ModuleNode(new Module(SOFTWARE, projectName));
		for (Module module : modules)
			addModule(module);
		projectResult.setSourceTree(sourceRoot);
	}

	private void addModule(Module module) {
		if (module.getGranularity() != SOFTWARE) {
			ModuleNode parent = addInferredAncestry(module);
			if (parent.hasChildFor(module))
				parent.getChildFor(module).setModule(module);
			else
				parent.addChild(new ModuleNode(module));
		}
	}

	private ModuleNode addInferredAncestry(Module module) {
		ModuleNode parent = sourceRoot;
		for (Module ancestor : module.inferAncestry()) {
			ModuleNode child;
			if (parent.hasChildFor(ancestor))
				child = parent.getChildFor(ancestor);
			else {
				child = new ModuleNode(ancestor);
				parent.addChild(child);
			}
			parent = child;
		}
		return parent;
	}
}