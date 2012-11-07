package org.kalibro.core.concurrent;

/**
 * Task for notifying task listener.
 * 
 * @author Carlos Morais
 */
class TaskListenerNotifier<T> extends VoidTask {

	private TaskReport<T> report;
	private TaskListener<T> listener;

	TaskListenerNotifier(TaskReport<T> report, TaskListener<T> listener) {
		this.report = report;
		this.listener = listener;
	}

	@Override
	protected void perform() {
		listener.taskFinished(report);
	}
}