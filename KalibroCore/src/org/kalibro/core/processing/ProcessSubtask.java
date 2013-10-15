package org.kalibro.core.processing;

import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.core.concurrent.VoidTask;

/**
 * Subtask of the whole {@link Processing}.
 * 
 * @author Carlos Morais
 */
abstract class ProcessSubtask extends VoidTask {

	ProcessContext context;

	ProcessSubtask(ProcessContext context) {
		this.context = context;
	}

	ProcessState getState() {
		return ProcessState.valueOf(getClass().getSimpleName().replace("Task", "").toUpperCase());
	}

	@Override
	public String toString() {
		return context.processing.getState().getMessage(context.repository.getCompleteName());
	}
}