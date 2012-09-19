package org.kalibro.core.processing;

import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskReport;
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

	protected T execute() {
		project.setState(getTaskState());
		DaoFactory.getProjectDao().save(project);
		return executeAndWait();
	}

	@Override
	public void setReport(TaskReport<T> report) {
		projectResult.setStateTime(getTaskState(), report.getExecutionTime());
		super.setReport(report);
	}

	protected abstract ProjectState getTaskState();

	@Override
	public String toString() {
		return project.getStateMessage();
	}
}