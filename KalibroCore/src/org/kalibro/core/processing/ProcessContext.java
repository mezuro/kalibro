package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.KalibroSettings;
import org.kalibro.Project;
import org.kalibro.Repository;

public class ProcessContext {

	private Repository repository;

	private File codeDirectory;

	ProcessContext(Repository repository) {
		this.repository = repository;
		establishCodeDirectory();
	}

	private void establishCodeDirectory() {
		Project project = repository.getProject();
		File loadDirectory = KalibroSettings.load().getServerSettings().getLoadDirectory();
		File projectDirectory = new File(loadDirectory, project.getName() + "-" + project.getId());
		codeDirectory = new File(projectDirectory, repository.getName() + "-" + repository.getId());
	}

	File codeDirectory() {
		return codeDirectory;
	}
}