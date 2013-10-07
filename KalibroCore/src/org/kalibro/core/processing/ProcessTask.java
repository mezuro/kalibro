package org.kalibro.core.processing;

import java.util.SortedSet;

import org.kalibro.*;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.RepositoryObserverDatabaseDao;
import org.kalibro.dao.DaoFactory;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask implements TaskListener<Void>, Observable {

	private Repository repository;
	private ProcessContext context;

	private SortedSet<RepositoryObserver> observers;

	public ProcessTask(Repository repository) {
		this.repository = repository;
		setObservers();
	}

	private void setObservers() {
		RepositoryObserverDatabaseDao repositoryObserverDatabaseDao =
			(RepositoryObserverDatabaseDao) DaoFactory.getRepositoryObserverDao();
		this.observers = repositoryObserverDatabaseDao.observersOf(repository.getId());
	}

	@Override
	protected void perform() {
		context = new ProcessContext(repository);
		new LoadingTask(context).addListener(this).execute();
		new CollectingTask(context).addListener(this).executeInBackground();
		new BuildingTask(context).addListener(this).execute();
		new AggregatingTask(context).addListener(this).execute();
		new CalculatingTask(context).addListener(this).execute();
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
		tryToNotify();
	}

	void tryToNotify() {
		if (!context.processing().getState().isTemporary())
			notifyObservers();
	}

	@Override
	public void notifyObservers() {
		for (RepositoryObserver observer : observers) {
			observer.update(repository, context.processing().getState());
		}
	}
}