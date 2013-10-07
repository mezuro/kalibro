package org.kalibro.core.processing;

import org.kalibro.*;
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
	protected void perform() {
		context = new ProcessContext(repository);
		new LoadingTask(context).addListener(this).execute();
		new CollectingTask(context).addListener(this).executeInBackground();
		new BuildingTask(context).addListener(this).execute();
		new AggregatingTask(context).addListener(this).execute();
		new CalculatingTask(context).addListener(this).execute();
		addRepositoryListeners();
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> report) {
		Processing processing = context.processing();
		ProcessState subtaskState = ((ProcessSubtask) report.getTask()).getState();
		processing.setStateTime(subtaskState, report.getExecutionTime());
		if (report.isTaskDone())
			processing.setState(subtaskState.nextState());
		else
			processing.setError(report.getError());
		context.processingDao().save(processing, repository.getId());
	}

	public Repository getRepository() {
		return repository;
	}

	public ProcessState getProcessState() {
		return context.processing().getState();
	}

	private void addRepositoryListeners() {
		for (RepositoryObserver listener : context.repositoryListeners())
			addListener(listener);
	}
}