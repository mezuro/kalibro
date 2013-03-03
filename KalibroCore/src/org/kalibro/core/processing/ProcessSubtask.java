package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.NativeModuleResult;
import org.kalibro.Processing;
import org.kalibro.Project;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Subtask of the whole {@link Processing}.
 * 
 * @author Carlos Morais
 */
abstract class ProcessSubtask extends VoidTask {

	private ProcessTask mainTask;

	ProcessSubtask prepare(ProcessTask task) {
		mainTask = task;
		addListener(mainTask);
		return this;
	}

	File codeDirectory() {
		return mainTask.codeDirectory;
	}

	void setCodeDirectory(File codeDirectory) {
		mainTask.codeDirectory = codeDirectory;
	}

	Project project() {
		return repository().getProject();
	}

	Repository repository() {
		return mainTask.repository;
	}

	Processing processing() {
		return mainTask.processing;
	}

	DatabaseDaoFactory daoFactory() {
		return mainTask.daoFactory;
	}

	Producer<NativeModuleResult> resultProducer() {
		return mainTask.resultProducer;
	}

	@Override
	public String toString() {
		return mainTask.processing.getStateMessage();
	}
}