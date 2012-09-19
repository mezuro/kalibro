package org.kalibro.core.concurrent;

import java.util.HashSet;
import java.util.Set;

public abstract class Task<T> implements Runnable {

	public static final long SECOND = 1000L;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;

	private TaskExecutor executor;
	private Set<TaskListener> listeners;

	protected TaskReport<T> report;

	public Task() {
		executor = new TaskExecutor(this);
		listeners = new HashSet<TaskListener>();
	}

	public void addListener(TaskListener listener) {
		listeners.add(listener);
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
		computeReport();
		for (TaskListener listener : listeners)
			reportTaskFinished(listener);
	}

	private void computeReport() {
		long start = System.currentTimeMillis();
		try {
			setReport(new TaskReport<T>(this, start, compute()));
		} catch (Throwable error) {
			setReport(new TaskReport<T>(this, start, error));
		}
	}

	public abstract T compute() throws Throwable;

	protected void setReport(TaskReport<T> report) {
		this.report = report;
	}

	public TaskReport<T> getReport() {
		return report;
	}

	protected void reportTaskFinished(final TaskListener listener) {
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