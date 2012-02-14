package org.kalibro.core.processing;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

public abstract class ProjectTaskExecutor implements TaskListener {

	protected ProjectResult projectResult;

	protected ProjectTaskExecutor(ProjectResult projectResult) {
		this.projectResult = projectResult;
	}

	public void execute() {
		updateProjectState(getTaskState());
		Task task = getTask();
		task.setListener(this);
		task.executeInBackground();
	}

	protected abstract ProjectState getTaskState();

	protected void updateProjectState(ProjectState newState) {
		getProject().setState(newState);
		saveProject();
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

	private void reportError(Exception error) {
		getProject().setError(error);
		saveProject();
	}

	private void saveProject() {
		Project project = getProject();
		Kalibro.getProjectDao().save(project);
		Kalibro.fireProjectStateChanged(project);
	}

	protected Project getProject() {
		return projectResult.getProject();
	}
}