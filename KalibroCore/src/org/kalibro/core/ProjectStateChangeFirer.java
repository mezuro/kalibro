package org.kalibro.core;

import org.kalibro.core.model.enums.ProjectState;

class ProjectStateChangeFirer implements Runnable {

	private String projectName;
	private ProjectState newProjectState;
	private ProjectStateListener listener;

	protected ProjectStateChangeFirer(String projectName, ProjectState newProjectState, ProjectStateListener listener) {
		this.projectName = projectName;
		this.newProjectState = newProjectState;
		this.listener = listener;
	}

	protected void fire() {
		new Thread(this).start();
	}

	@Override
	public void run() {
		listener.projectStateChanged(projectName, newProjectState);
	}
}