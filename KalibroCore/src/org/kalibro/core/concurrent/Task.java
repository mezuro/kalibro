package org.kalibro.core.concurrent;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * A task that computes a result and may throw an error. This class implements the {@link Runnable} interface because
 * its instances are potentially executed by another thread. It contains utility methods to execute in background, with
 * timeouts and periodically, without worrying about the mechanics of its execution.
 * 
 * @author Carlos Morais
 */
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
		future = TaskExecutor.executeInBackground(this);
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

	public void cancelExecution() {
		future.cancel(true);
	}

	TaskReport<T> getReport() {
		return report;
	}

	@Override
	public void run() {
		computeReport();
		reportTaskFinished();
	}

	private void computeReport() {
		long start = System.currentTimeMillis();
		try {
			report = new TaskReport<T>(this, start, compute());
		} catch (Throwable error) {
			report = new TaskReport<T>(this, start, error);
		}
	}

	protected abstract T compute() throws Throwable;

	private void reportTaskFinished() {
		for (TaskListener<T> listener : listeners)
			new TaskListenerNotifier<T>(report, listener).executeInBackground();
	}

	@Override
	public String toString() {
		return "running task: " + getClass().getCanonicalName();
	}
}