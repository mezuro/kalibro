package org.kalibro.core.processing;

import org.kalibro.Project;
import org.kalibro.Processing;
import org.kalibro.ProcessState;
import org.kalibro.core.concurrent.Task;
import org.kalibro.dao.DaoFactory;

abstract class ProcessProjectSubtask<T> extends Task<T> {

	protected Project project;
	protected Processing processing;

	protected ProcessProjectSubtask(Processing processing) {
		this.processing = processing;
		project = processing.getRepository();
	}

	T executeSubTask() {
		project.setState(getTaskState());
		DaoFactory.getProjectDao().save(project);
		return execute();
	}

	@Override
	public void reportTaskFinished() {
		processing.setStateTime(getTaskState(), getReport().getExecutionTime());
		super.reportTaskFinished();
	}

	protected abstract ProcessState getTaskState();

	@Override
	public String toString() {
		return project.getStateMessage();
	}
}