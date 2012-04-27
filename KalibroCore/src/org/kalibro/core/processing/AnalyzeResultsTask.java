package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

class AnalyzeResultsTask extends ProcessProjectSubtask<Collection<ModuleResult>> {

	private Map<Module, ModuleResult> resultMap;

	protected AnalyzeResultsTask(ProjectResult projectResult, Map<Module, ModuleResult> resultMap) {
		super(projectResult);
		this.resultMap = resultMap;
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.ANALYZING;
	}

	@Override
	public Collection<ModuleResult> performAndGetResult() {
		new SourceTreeBuilder(projectResult).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(projectResult, resultMap).aggregate();
		return resultMap.values();
	}
}