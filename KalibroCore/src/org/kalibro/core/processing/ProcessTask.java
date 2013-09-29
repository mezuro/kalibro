package org.kalibro.core.processing;

import org.kalibro.Configuration;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask implements TaskListener<Void> {

	private Repository repository;
	private ProcessContext context;

	public ProcessTask(Repository repository) {
		this.repository = repository;
	}

	@Override
	protected void perform() throws Exception {
		context = new ProcessContext(repository);
		createSubtask(LoadingTask.class).execute();
		createSubtask(CollectingTask.class).executeInBackground();
		createSubtask(BuildingTask.class).execute();
		createSubtask(AggregatingTask.class).execute();
		createSubtask(CalculatingTask.class).execute();
	}

	private ProcessSubtask createSubtask(Class<? extends ProcessSubtask> subtaskClass) throws Exception {
		ProcessSubtask subtask = subtaskClass.getConstructor(ProcessContext.class).newInstance(context);
		subtask.addListener(this);
		return subtask;
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> report) {
		updateProcessing(report);
		context.processingDao().save(context.processing(), repository.getId());
	}

	private void updateProcessing(TaskReport<Void> report) {
		Processing processing = context.processing();
		ProcessState subtaskState = ((ProcessSubtask) report.getTask()).getState();
		processing.setStateTime(subtaskState, report.getExecutionTime());
		if (!processing.getState().isTemporary())
			return;
		if (report.isTaskDone())
			processing.setState(subtaskState.nextState());
		else
			processing.setError(report.getError());
	}
}