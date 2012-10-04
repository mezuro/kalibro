package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.RepositoryResult;
import org.kalibro.ProcessState;

class LoadSourceTask extends ProcessProjectSubtask<RepositoryResult> {

	protected LoadSourceTask(Project project) {
		super(new RepositoryResult(project));
	}

	@Override
	protected ProcessState getTaskState() {
		return ProcessState.LOADING;
	}

	@Override
	protected RepositoryResult compute() {
		project.load();
		return repositoryResult;
	}
}