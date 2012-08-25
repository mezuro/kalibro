package org.kalibro.core.processing;

import org.kalibro.core.Kalibro;
import org.kalibro.core.concurrent.TypedTask;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

abstract class ProcessProjectSubtask<T> extends TypedTask<T> {

	protected Project project;
	protected ProjectResult projectResult;

	protected ProcessProjectSubtask(ProjectResult projectResult) {
		this.projectResult = projectResult;
		project = projectResult.getProject();
	}

	protected T execute() {
		project.setState(getTaskState());
		Kalibro.getProjectDao().save(project);
		return executeAndWaitResult();
	}

	@Override
	protected void setReport(long executionTime, Throwable error) {
		projectResult.setStateTime(getTaskState(), executionTime);
		super.setReport(executionTime, error);
	}

	protected abstract ProjectState getTaskState();

	@Override
	public String toString() {
		return project.getStateMessage();
	}
}