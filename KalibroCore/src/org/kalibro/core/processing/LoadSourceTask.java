package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.ProcessState;
import org.kalibro.Processing;

class LoadSourceTask extends ProcessSubtask<File> {

	LoadSourceTask(Processing processing) {
		super(processing);
	}

	@Override
	protected File compute() {
		File codeDirectory;
		project.load();
		return codeDirectory;
	}

	@Override
	ProcessState getNextState() {
		return ProcessState.COLLECTING;
	}
}