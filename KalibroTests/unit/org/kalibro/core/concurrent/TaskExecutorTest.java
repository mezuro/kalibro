package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.KalibroTestCase;

public class TaskExecutorTest extends KalibroTestCase implements TaskListener {

	private static final long TIMEOUT = UNIT_TIMEOUT / 5;

	private TaskReport report;

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReportForNormalBackgroundExecution() throws InterruptedException {
		executeInBackgroundAndGetReport(new DoNothingTask());
		assertTrue(report.isTaskDone());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldGetReportForAbnormalBackgroundExecution() throws InterruptedException {
		executeInBackgroundAndGetReport(new ThrowErrorTask());
		assertFalse(report.isTaskDone());
		assertNotNull(report.getError());
	}

	private synchronized void executeInBackgroundAndGetReport(Task task) throws InterruptedException {
		report = null;
		task.setListener(this);
		new TaskExecutor(task).executeInBackground();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport taskReport) {
		report = taskReport;
		notifyTest();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteAndWaitWithoutTimeout() {
		new TaskExecutor(new DoNothingTask()).executeAndWait();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowSameKalibroExceptionThrownByTask() {
		String message = "TaskExecutorTest message";
		final KalibroException error = new KalibroException(message, new Throwable());
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(error)).executeAndWait();
			}
		}, message, Throwable.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionWrappingOtherError() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(new Throwable())).executeAndWait();
			}
		}, "Error while throwing error", Throwable.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteAndWaitWithTimeout() {
		new TaskExecutor(new DoNothingTask()).executeAndWait(TIMEOUT);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowSameKalibroExceptionThrownByTaskWithTimeout() {
		String message = "TaskExecutorTest message";
		final KalibroException error = new KalibroException(message, new Throwable());
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(error)).executeAndWait(TIMEOUT);
			}
		}, message, Throwable.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionForTimeoutError() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new TaskExecutor(new SleepTask(UNIT_TIMEOUT)).executeAndWait(TIMEOUT);
			}
		}, "Timed out after " + TIMEOUT + " milliseconds while sleeping", InterruptedException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldThrowKalibroExceptionWrappingOtherErrorWithTimeout() {
		checkKalibroException(new Task() {

			@Override
			protected void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(new Throwable())).executeAndWait(TIMEOUT);
			}
		}, "Error while throwing error", Throwable.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testPeriodicExecution() throws InterruptedException {
		IncrementResultTask task = new IncrementResultTask();
		TaskExecutor executor = new TaskExecutor(task);

		long period = 50;
		executor.executePeriodically(period);

		Thread.sleep(period / 2);
		assertEquals(1, task.result);

		Thread.sleep(period);
		assertEquals(2, task.result);

		Thread.sleep(period);
		assertEquals(3, task.result);

		executor.cancelPeriodicExecution();
		Thread.sleep(period);
		assertEquals(3, task.result);
	}

	private class IncrementResultTask extends TypedTask<Integer> {

		private int result;

		@Override
		public Integer performAndGetResult() {
			return result++;
		}
	}
}