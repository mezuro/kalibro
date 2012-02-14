package org.kalibro.core.processing;

import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

public class LoadProjectExecutor extends ProjectTaskExecutor {

	public LoadProjectExecutor(Project project) {
		super(new ProjectResult(project));
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.LOADING;
	}

	@Override
	protected Task getTask() {
		return new LoadProjectTask(getProject());
	}

	@Override
	protected void setTaskExecutionTime(long executionTime) {
		projectResult.setLoadTime(executionTime);
	}

	@Override
	protected void continueProcessing(TaskReport report) {
		new AnalyzeProjectExecutor(projectResult).execute();
	}
}