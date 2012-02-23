package org.kalibro.core;

import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.enums.ProjectState;

class ProjectStateChangeFirer extends Task {

	private String projectName;
	private ProjectState newProjectState;
	private ProjectStateListener listener;

	protected ProjectStateChangeFirer(String projectName, ProjectState newProjectState, ProjectStateListener listener) {
		this.projectName = projectName;
		this.newProjectState = newProjectState;
		this.listener = listener;
	}

	@Override
	public void perform() {
		listener.projectStateChanged(projectName, newProjectState);
	}
}