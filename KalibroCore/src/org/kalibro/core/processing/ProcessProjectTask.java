package org.kalibro.core.processing;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;

public class ProcessProjectTask extends Task {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = Kalibro.getProjectDao().getProject(projectName);
	}

	@Override
	public void perform() {
		// TODO execute all parts of project processing in the same thread
		new LoadProjectExecutor(project).execute();
	}
}