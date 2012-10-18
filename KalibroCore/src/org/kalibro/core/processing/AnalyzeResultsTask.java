package org.kalibro.core.processing;

import java.util.Collection;
import java.util.Map;

import org.kalibro.Module;
import org.kalibro.ModuleResult;
import org.kalibro.Processing;
import org.kalibro.ProcessState;

class AnalyzeResultsTask extends ProcessSubtask<Collection<ModuleResult>> {

	private Map<Module, ModuleResult> resultMap;

	protected AnalyzeResultsTask(Processing processing, Map<Module, ModuleResult> resultMap) {
		super(processing);
		this.resultMap = resultMap;
	}

	@Override
	protected ProcessState getTaskState() {
		return ProcessState.ANALYZING;
	}

	@Override
	protected Collection<ModuleResult> compute() {
		new SourceTreeBuilder(processing).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(processing, resultMap).aggregate();
		return resultMap.values();
	}
}