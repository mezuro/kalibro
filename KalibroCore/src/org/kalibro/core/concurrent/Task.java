package org.kalibro.core.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public abstract class Task<T> implements Runnable {

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

	public T execute() {
		return TaskExecutor.execute(this);
	}

	public T execute(long timeout, TimeUnit timeUnit) {
		return TaskExecutor.execute(this, timeout, timeUnit);
	}

	public void executePeriodically(long period, TimeUnit timeUnit) {
		future = TaskExecutor.executePeriodically(this, period, timeUnit);
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