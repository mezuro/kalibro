package org.kalibro.core.processing;

import org.kalibro.*;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.concurrent.TaskListener;
import org.kalibro.core.concurrent.TaskReport;
import org.kalibro.core.concurrent.VoidTask;

/**
 * Performs a {@link Processing} for a {@link Repository} according to its {@link Configuration}.
 * 
 * @author Carlos Morais
 */
public class ProcessTask extends VoidTask implements TaskListener<Void> {

	private ProcessContext context;

	public ProcessTask(Repository repository) {
		context = new ProcessContext(repository);
		addListener(this);
	}

	@Override
	protected void perform() throws Exception {
		new PreparingTask(context).addListener(this).execute();
		new LoadingTask(context).addListener(this).execute();
		new CollectingTask(context).addListener(this).executeInBackground();
		new BuildingTask(context).addListener(this).execute();
		new AggregatingTask(context).addListener(this).execute();
		new CalculatingTask(context).addListener(this).execute();
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> report) {
		if (report.getTask() == this)
			executeNotificationCommand();
		else
			subtaskFinished(report);
	}

	private void executeNotificationCommand() {
		String notificationCommand = KalibroSettings.load().getServerSettings().getNotificationCommand();
		if (!notificationCommand.trim().isEmpty())
			new CommandTask(notificationCommand).execute();
	}

	private void subtaskFinished(TaskReport<Void> report) {
		Processing processing = context.processing;
		ProcessState subtaskState = ((ProcessSubtask) report.getTask()).getState();
		processing.setStateTime(subtaskState, report.getExecutionTime());
		if (report.isTaskDone())
			processing.setState(subtaskState.nextState());
		else
			processing.setError(report.getError());
		context.processingDao.save(processing, context.repository.getId());
	}
}