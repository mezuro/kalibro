package org.kalibro.core.processing;

import java.util.Set;

import org.kalibro.NativeModuleResult;
import org.kalibro.ProcessState;
import org.kalibro.Processing;

class AnalyzeResultsTask extends ProcessSubtask<Void> {

	private Set<NativeModuleResult> results;

	AnalyzeResultsTask(Processing processing, Set<NativeModuleResult> results) {
		super(processing);
		this.results = results;
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.READY;
	}

	@Override
	protected Void compute() {
		new SourceTreeBuilder(processing).buildSourceTree(resultMap.keySet());
		new ResultsAggregator(processing, resultMap).aggregate();
		return null;
	}
}