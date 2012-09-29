package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.ProjectResult;
import org.kalibro.ProjectState;

class LoadSourceTask extends ProcessProjectSubtask<ProjectResult> {

	protected LoadSourceTask(Project project) {
		super(new ProjectResult(project));
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.LOADING;
	}

	@Override
	protected ProjectResult compute() {
		project.load();
		return projectResult;
	}
}