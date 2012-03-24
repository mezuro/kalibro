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

	public void executeAndWait() {
		executor.executeAndWait();
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
		long start = System.currentTimeMillis();
		Throwable error = performAndGetError();
		long executionTime = System.currentTimeMillis() - start;
		if (listener != null)
			reportTaskFinished(new TaskReport(executionTime, error));
	}

	private Throwable performAndGetError() {
		try {
			perform();
			return null;
		} catch (Throwable exception) {
			return exception;
		}
	}

	protected abstract void perform() throws Throwable;

	protected void reportTaskFinished(final TaskReport report) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				listener.taskFinished(report);
			}
		}).start();
	}

	@Override
	public String toString() {
		return "running task: " + getClass().getCanonicalName();
	}
}