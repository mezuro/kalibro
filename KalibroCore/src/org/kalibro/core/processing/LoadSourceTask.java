package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.RepositoryResult;
import org.kalibro.RepositoryState;

class LoadSourceTask extends ProcessProjectSubtask<RepositoryResult> {

	protected LoadSourceTask(Project project) {
		super(new RepositoryResult(project));
	}

	@Override
	protected RepositoryState getTaskState() {
		return RepositoryState.LOADING;
	}

	@Override
	protected RepositoryResult compute() {
		project.load();
		return repositoryResult;
	}
}