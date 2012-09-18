package org.kalibro.core.concurrent;

public abstract class Task implements Runnable {

	public static final long SECOND = 1000L;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;

	private TaskListener listener;
	private TaskExecutor executor;

	protected TaskReport<?> report;

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
		performAndSetReport();
		if (listener != null)
			reportTaskFinished();
	}

	private void performAndSetReport() {
		long start = System.currentTimeMillis();
		try {
			perform();
			setReport(new TaskReport<Object>(this, start, (Object) null));
		} catch (Throwable exception) {
			setReport(new TaskReport<Object>(this, start, exception));
		}
	}

	public abstract void perform() throws Throwable;

	protected void setReport(TaskReport<?> report) {
		this.report = report;
	}

	public TaskReport<?> getReport() {
		return report;
	}

	protected void reportTaskFinished() {
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