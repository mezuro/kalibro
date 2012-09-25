package org.kalibro.core.processing;

import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.dao.DaoFactory;

abstract class ProcessProjectSubtask<T> extends Task<T> {

	protected Project project;
	protected ProjectResult projectResult;

	protected ProcessProjectSubtask(ProjectResult projectResult) {
		this.projectResult = projectResult;
		project = projectResult.getProject();
	}

	T executeSubTask() {
		project.setState(getTaskState());
		DaoFactory.getProjectDao().save(project);
		return execute();
	}

	@Override
	public void reportTaskFinished() {
		projectResult.setStateTime(getTaskState(), getReport().getExecutionTime());
		super.reportTaskFinished();
	}

	protected abstract ProjectState getTaskState();

	@Override
	public String toString() {
		return project.getStateMessage();
	}
}