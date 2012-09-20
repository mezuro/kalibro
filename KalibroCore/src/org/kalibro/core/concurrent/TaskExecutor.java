package org.kalibro.core.concurrent;

import java.util.concurrent.*;

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
		return scheduleAndGet(task, null, null);
	}

	private static <T> T scheduleAndGet(Task<T> task, Long timeout, TimeUnit timeUnit) {
		try {
			doScheduleAndGet(task, timeout, timeUnit);
		} catch (TimeoutException exception) {
			String timeoutString = timeout + " " + timeUnit.name().toLowerCase();
			throw new KalibroException("Timed out after " + timeoutString + " while " + task, exception);
		} catch (Exception exception) {
			throw new KalibroException("Error while " + task, exception);
		}
		TaskReport<T> report = task.getReport();
		if (report.isTaskDone())
			return report.getResult();
		throw new KalibroException("Error while " + task, report.getError());
	}

	private static void doScheduleAndGet(Task<?> task, Long timeout, TimeUnit timeUnit) throws Exception {
		Future<?> future = scheduleForNow(task);
		if (timeout == null)
			future.get();
		else
			future.get(timeout, timeUnit);
	}

	private static Future<?> scheduleForNow(Task<?> task) {
		return EXECUTOR.schedule(task, 0, TimeUnit.NANOSECONDS);
	}

	private TaskExecutor() {
		return;
	}
}