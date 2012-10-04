package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.RepositoryResult;
import org.kalibro.ResultState;

class LoadSourceTask extends ProcessProjectSubtask<RepositoryResult> {

	protected LoadSourceTask(Project project) {
		super(new RepositoryResult(project));
	}

	@Override
	protected ResultState getTaskState() {
		return ResultState.LOADING;
	}

	@Override
	protected RepositoryResult compute() {
		project.load();
		return repositoryResult;
	}
}