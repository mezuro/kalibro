package org.kalibro.core.processing;

import java.io.IOException;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;

class LoadProjectTask extends Task {

	private Project project;

	protected LoadProjectTask(Project project) {
		this.project = project;
	}

	@Override
	public void perform() throws IOException {
		project.load(Kalibro.currentSettings().getLoadDirectoryFor(project));
	}

	@Override
	public String toString() {
		return "loading project: " + project.getName();
	}
}