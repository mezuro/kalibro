package org.kalibro.core.processing;

import java.io.File;

import org.kalibro.*;
import org.kalibro.core.concurrent.Producer;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;
import org.kalibro.core.persistence.DatabaseDaoFactory;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask implements TaskListener<Void> {

	File codeDirectory;
	Repository repository;
	Processing processing;
	DatabaseDaoFactory daoFactory;
	Producer<NativeModuleResult> resultProducer;

	public ProcessTask(Repository repository) {
		this.repository = repository;
	}

	@Override
	protected void perform() {
		daoFactory = new DatabaseDaoFactory();
		processing = daoFactory.createProcessingDao().createProcessingFor(repository);
		resultProducer = new Producer<NativeModuleResult>();
		new LoadingTask().prepare(this).execute();
		new CollectingTask().prepare(this).executeInBackground();
		ProcessSubtask analyzingTask = new AnalyzingTask().prepare(this);
		analyzingTask.addListener(new MailSender(repository));
		analyzingTask.execute();
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> report) {
		processing.setStateTime(getTaskState(report), report.getExecutionTime());
		if (processing.getState().isTemporary())
			updateState(report);
		daoFactory.createProcessingDao().save(processing, repository.getId());
	}

	private void updateState(TaskReport<Void> report) {
		if (report.isTaskDone())
			processing.setState(getTaskState(report).nextState());
		else
			processing.setError(report.getError());
	}

	private ProcessState getTaskState(TaskReport<Void> report) {
		String taskClassName = report.getTask().getClass().getSimpleName();
		return ProcessState.valueOf(taskClassName.replace("Task", "").toUpperCase());
	}
}