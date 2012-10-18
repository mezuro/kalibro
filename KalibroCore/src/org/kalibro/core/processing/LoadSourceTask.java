package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.Processing;
import org.kalibro.ProcessState;

class LoadSourceTask extends ProcessSubtask<Processing> {

	protected LoadSourceTask(Project project) {
		super(new Processing(project));
	}

	@Override
	protected ProcessState getTaskState() {
		return ProcessState.LOADING;
	}

	@Override
	protected Processing compute() {
		project.load();
		return processing;
	}
}