package org.kalibro.core.processing;

import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;

class LoadSourceTask extends ProcessProjectSubtask<ProjectResult> {

	protected LoadSourceTask(Project project) {
		super(new ProjectResult(project));
	}

	@Override
	protected ProjectState getTaskState() {
		return ProjectState.LOADING;
	}

	@Override
	protected ProjectResult performAndGetResult() {
		project.load();
		return projectResult;
	}
}