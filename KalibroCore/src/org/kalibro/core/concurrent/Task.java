package org.kalibro.core.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class Task<T> implements Runnable {

	public static final long SECOND = 1000L;
	public static final long MINUTE = 60 * SECOND;
	public static final long HOUR = 60 * MINUTE;
	public static final long DAY = 24 * HOUR;

	private Future<?> future;
	private TaskReport<T> report;
	private Set<TaskListener<T>> listeners;

	public Task() {
		listeners = new HashSet<TaskListener<T>>();
	}

	public void addListener(TaskListener<T> listener) {
		listeners.add(listener);
	}

	public void executeInBackground() {
		TaskExecutor.executeInBackground(this);
	}

	public T executeAndWait() {
		return TaskExecutor.execute(this);
	}

	public T executeAndWait(long timeout) {
		return TaskExecutor.execute(this, timeout, TimeUnit.MILLISECONDS);
	}

	public void executePeriodically(long period) {
		future = TaskExecutor.executePeriodically(this, period, TimeUnit.MILLISECONDS);
	}

	public void cancelPeriodicExecution() {
		future.cancel(false);
	}

	@Override
	public void run() {
		computeReport();
		for (TaskListener<T> listener : listeners)
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

	protected abstract T compute() throws Throwable;

	protected void setReport(TaskReport<T> report) {
		this.report = report;
	}

	public TaskReport<T> getReport() {
		return report;
	}

	protected void reportTaskFinished(final TaskListener<T> listener) {
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