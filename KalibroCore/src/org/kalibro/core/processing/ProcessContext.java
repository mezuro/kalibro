package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.KalibroSettings;
import org.kalibro.NativeModuleResult;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;

class ProcessContext {

	private Repository repository;

	private File codeDirectory;
	private Producer<NativeModuleResult> resultProducer;

	ProcessContext(Repository repository) {
		this.repository = repository;
		this.resultProducer = new Producer<NativeModuleResult>();
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

	Producer<NativeModuleResult> resultProducer() {
		return resultProducer;
	}
}