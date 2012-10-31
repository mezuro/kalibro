package org.kalibro.core.processing;

import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.core.concurrent.Task;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.persistence.DatabaseDaoFactory;
import org.kalibro.core.persistence.ProcessingDatabaseDao;

/**
 * Subtask of the whole {@link Processing}.
 * 
 * @author Carlos Morais
 */
abstract class ProcessSubtask<T> extends Task<T> implements TaskListener<T> {

	Processing processing;
	ProcessingDatabaseDao processingDao;

	ProcessSubtask(Processing processing) {
		this.processing = processing;
		this.processingDao = new DatabaseDaoFactory().createProcessingDao();
		addListener(this);
	}

	@Override
	public void taskFinished(TaskReport<T> report) {
		synchronized (processing) {
			processing.setStateTime(getTaskState(), report.getExecutionTime());
			if (processing.getState().isTemporary())
				updateState(report);
			processingDao.save(processing);
		}
	}

	private void updateState(TaskReport<?> report) {
		if (report.isTaskDone())
			processing.setState(getTaskState().nextState());
		else
			processing.setError(report.getError());
	}

	private ProcessState getTaskState() {
		return ProcessState.valueOf(getClass().getSimpleName().replace("Task", "").toUpperCase());
	}

	@Override
	public String toString() {
		return processing.getStateMessage();
	}
}