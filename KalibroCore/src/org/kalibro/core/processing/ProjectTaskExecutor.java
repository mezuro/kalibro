package org.kalibro.core.processing;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

abstract class ProjectTaskExecutor implements TaskListener {

	protected ProjectResult projectResult;

	protected ProjectTaskExecutor(ProjectResult projectResult) {
		this.projectResult = projectResult;
	}

	protected void execute() {
		updateProjectState(getTaskState());
		Task task = getTask();
		task.setListener(this);
		task.executeInBackground();
	}

	protected abstract ProjectState getTaskState();

	protected void updateProjectState(ProjectState newState) {
		getProject().setState(newState);
		Kalibro.getProjectDao().save(getProject());
	}

	protected abstract Task getTask();

	@Override
	public void taskFinished(TaskReport report) {
		setTaskExecutionTime(report.getExecutionTime());
		if (report.isTaskDone())
			continueProcessing(report);
		else
			reportError(report.getError());
	}

	protected abstract void setTaskExecutionTime(long executionTime);

	protected abstract void continueProcessing(TaskReport report);

	private void reportError(Throwable error) {
		getProject().setError(error);
		Kalibro.getProjectDao().save(getProject());
	}

	protected Project getProject() {
		return projectResult.getProject();
	}
}