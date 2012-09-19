package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.kalibro.UtilityClassTest;

public class TaskExecutorTest extends UtilityClassTest implements TaskListener<Void> {

	private static final long TIMEOUT = 200;

	private TaskReport<?> report;

	@Override
	protected Class<?> utilityClass() {
		return TaskExecutor.class;
	}

	@Test
	public void shouldGetReportForNormalBackgroundExecution() throws InterruptedException {
		executeInBackgroundAndGetReport(new DoNothingTask());
		assertTrue(report.isTaskDone());
	}

	@Test
	public void shouldGetReportForAbnormalBackgroundExecution() throws InterruptedException {
		executeInBackgroundAndGetReport(new ThrowErrorTask());
		assertFalse(report.isTaskDone());
		assertNotNull(report.getError());
	}

	private synchronized void executeInBackgroundAndGetReport(VoidTask task) throws InterruptedException {
		report = null;
		task.addListener(this);
		TaskExecutor.executeInBackground(task);
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> taskReport) {
		report = taskReport;
		notifyTest();
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		TaskExecutor.execute(new DoNothingTask());
	}

	@Test
	public void shouldThrowKalibroExceptionWrappingTaskError() {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				TaskExecutor.execute(new ThrowErrorTask(new Throwable()));
			}
		}).throwsException().withMessage("Error while throwing error").withCause(Throwable.class);
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		TaskExecutor.execute(new DoNothingTask(), TIMEOUT, TimeUnit.MILLISECONDS);
	}

	@Test
	public void shouldThrowKalibroExceptionForTimeoutError() {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				TaskExecutor.execute(new SleepTask(2 * TIMEOUT), TIMEOUT, TimeUnit.MILLISECONDS);
			}
		}).throwsException().withMessage("Timed out after " + TIMEOUT + " milliseconds while sleeping")
			.withCause(TimeoutException.class);
	}

	@Test
	public void testPeriodicExecution() throws InterruptedException {
		long period = 50;
		CounterTask task = new CounterTask();
		task.executePeriodically(period);

		Thread.sleep(period / 2);
		assertEquals(1, task.result);

		Thread.sleep(period);
		assertEquals(2, task.result);

		Thread.sleep(period);
		assertEquals(3, task.result);

		task.cancelPeriodicExecution();
		Thread.sleep(period);
		assertEquals(3, task.result);
	}
}