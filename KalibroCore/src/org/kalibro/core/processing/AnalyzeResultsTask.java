package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.RepositoryResult;
import org.kalibro.ProcessState;

class AnalyzeResultsTask extends ProcessProjectSubtask<Collection<ModuleResult>> {

	private Map<Module, ModuleResult> resultMap;

	protected AnalyzeResultsTask(RepositoryResult repositoryResult, Map<Module, ModuleResult> resultMap) {
		super(repositoryResult);
		this.resultMap = resultMap;
	}

	@Override
	protected ProcessState getTaskState() {
		return ProcessState.ANALYZING;
	}

	@Override
	protected Collection<ModuleResult> compute() {
		new SourceTreeBuilder(repositoryResult).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(repositoryResult, resultMap).aggregate();
		return resultMap.values();
	}
}