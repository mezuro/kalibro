package org.kalibro.core.concurrent;

import java.util.concurrent.*;

import org.kalibro.KalibroError;
import org.kalibro.KalibroException;

final class TaskExecutor {

	private static final int THREAD_POOL_SIZE = 100;
	private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(THREAD_POOL_SIZE);

	static void executeInBackground(Task<?> task) {
		scheduleForNow(task);
	}

	static <T> T execute(Task<T> task) {
		return scheduleAndGet(task);
	}

	static <T> T execute(Task<T> task, long timeout, TimeUnit timeUnit) {
		return scheduleAndGet(task, timeout, timeUnit);
	}

	static Future<?> executePeriodically(Task<?> task, long period, TimeUnit timeUnit) {
		return EXECUTOR.scheduleAtFixedRate(task, 0, period, timeUnit);
	}

	private static <T> T scheduleAndGet(Task<T> task) {
		return scheduleAndGet(task, 1, TimeUnit.DAYS);
	}

	private static <T> T scheduleAndGet(Task<T> task, long timeout, TimeUnit timeUnit) {
		try {
			scheduleForNow(task).get(timeout, timeUnit);
		} catch (ExecutionException exception) {
			throw new KalibroError("Error while " + task + "\nOverriden Task.run() threw an exception.", exception);
		} catch (InterruptedException exception) {
			throw new KalibroException("Thread interrupted while waiting for task to finish: " + task, exception);
		} catch (TimeoutException exception) {
			String message = "Timed out after " + timeout + " " + timeUnit.name().toLowerCase() + " while " + task;
			throw new KalibroException(message, exception);
		}
		return resultOf(task);
	}

	private static Future<?> scheduleForNow(Task<?> task) {
		return EXECUTOR.schedule(task, 0, TimeUnit.NANOSECONDS);
	}

	private static <T> T resultOf(Task<T> task) {
		TaskReport<T> report = task.getReport();
		if (report.isTaskDone())
			return report.getResult();
		throw new KalibroException("Error while " + task, report.getError());
	}

	private TaskExecutor() {
		return;
	}
}