package org.kalibro.core.processing;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.kalibro.Kalibro;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.model.Project;

class LoadProjectTask extends Task {

	private static final long LOAD_TIMEOUT = 1 * HOUR;

	private Project project;

	protected LoadProjectTask(Project project) {
		this.project = project;
	}

	@Override
	public void perform() throws IOException {
		executeLoadCommands(prepareLoadDirectory());
	}

	private String prepareLoadDirectory() throws IOException {
		File loadDirectory = Kalibro.currentSettings().getLoadDirectoryFor(project);
		loadDirectory.mkdirs();
		FileUtils.cleanDirectory(loadDirectory);
		return loadDirectory.getAbsolutePath();
	}

	private void executeLoadCommands(String loadPath) {
		for (String loadCommand : project.getLoadCommands(loadPath))
			new CommandTask(loadCommand).executeAndWait(LOAD_TIMEOUT);
	}

	@Override
	public String toString() {
		return "loading project: " + project.getName();
	}
}