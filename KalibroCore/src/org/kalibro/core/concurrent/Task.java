package org.kalibro.core.concurrent;

public abstract class Task implements Runnable {

	public static final long SECOND = 1000L;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;

	private TaskListener listener;
	private TaskExecutor executor;

	public Task() {
		this.executor = new TaskExecutor(this);
	}

	public void setListener(TaskListener listener) {
		this.listener = listener;
	}

	public void executeInBackground() {
		executor.executeInBackground();
	}

	public void executeAndWait(long timeout) {
		executor.executeAndWait(timeout);
	}

	public void executePeriodically(long period) {
		executor.executePeriodically(period);
	}

	public void cancelPeriodicExecution() {
		executor.cancelPeriodicExecution();
	}

	@Override
	public void run() {
		Exception error = null;
		long start = System.currentTimeMillis();
		try {
			perform();
		} catch (Exception exception) {
			error = exception;
		}
		long executionTime = System.currentTimeMillis() - start;
		reportTaskFinished(new TaskReport(executionTime, error));
	}

	public abstract void perform() throws Exception;

	protected void reportTaskFinished(final TaskReport report) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				if (listener != null)
					listener.taskFinished(report);
			}
		}).start();
	}

	@Override
	public String toString() {
		return "running task: " + getClass();
	}
}