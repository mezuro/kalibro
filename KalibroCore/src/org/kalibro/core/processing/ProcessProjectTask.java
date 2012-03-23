package org.kalibro.core.processing;

import org.kalibro.Kalibro;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;

/*
 * TODO Make possible to add listener to be notified when whole process finishes. This is desirable for
 * integration-testing the whole process. Not possible yet because each sub-task is run in different threads with
 * different listeners, and this task just starts the others.
 */
public class ProcessProjectTask extends Task {

	private Project project;

	public ProcessProjectTask(String projectName) {
		project = Kalibro.getProjectDao().getProject(projectName);
	}

	@Override
	public void perform() {
		new LoadProjectExecutor(project).execute();
	}
}