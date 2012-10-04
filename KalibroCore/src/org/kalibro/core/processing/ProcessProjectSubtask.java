package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.RepositoryResult;
import org.kalibro.RepositoryState;
import org.kalibro.core.concurrent.Task;
import org.kalibro.dao.DaoFactory;

abstract class ProcessProjectSubtask<T> extends Task<T> {

	protected Project project;
	protected RepositoryResult repositoryResult;

	protected ProcessProjectSubtask(RepositoryResult repositoryResult) {
		this.repositoryResult = repositoryResult;
		project = repositoryResult.getProject();
	}

	T executeSubTask() {
		project.setState(getTaskState());
		DaoFactory.getProjectDao().save(project);
		return execute();
	}

	@Override
	public void reportTaskFinished() {
		repositoryResult.setStateTime(getTaskState(), getReport().getExecutionTime());
		super.reportTaskFinished();
	}

	protected abstract RepositoryState getTaskState();

	@Override
	public String toString() {
		return project.getStateMessage();
	}
}