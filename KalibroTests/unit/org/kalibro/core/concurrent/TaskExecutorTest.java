package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroException;
import org.kalibro.TestCase;

public class TaskExecutorTest extends TestCase implements TaskListener {

	private static final long TIMEOUT = 200;

	private TaskReport<?> report;

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

	private synchronized void executeInBackgroundAndGetReport(Task task) throws InterruptedException {
		report = null;
		task.addListener(this);
		new TaskExecutor(task).executeInBackground();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport<?> taskReport) {
		report = taskReport;
		notifyTest();
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		new TaskExecutor(new DoNothingTask()).executeAndWait();
	}

	@Test
	public void shouldThrowSameKalibroExceptionThrownByTask() {
		String message = "TaskExecutorTest message";
		final KalibroException error = new KalibroException(message, new Throwable());
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(error)).executeAndWait();
			}
		}).throwsException().withMessage(message).withCause(Throwable.class);
	}

	@Test
	public void shouldThrowKalibroExceptionWrappingOtherError() {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(new Throwable())).executeAndWait();
			}
		}).throwsException().withMessage("Error while throwing error").withCause(Throwable.class);
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		new TaskExecutor(new DoNothingTask()).executeAndWait(TIMEOUT);
	}

	@Test
	public void shouldThrowSameKalibroExceptionThrownByTaskWithTimeout() {
		String message = "TaskExecutorTest message";
		final KalibroException error = new KalibroException(message, new Throwable());
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(error)).executeAndWait(TIMEOUT);
			}
		}).throwsException().withMessage(message).withCause(Throwable.class);
	}

	@Test
	public void shouldThrowKalibroExceptionForTimeoutError() {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				new TaskExecutor(new SleepTask(2 * TIMEOUT)).executeAndWait(TIMEOUT);
			}
		}).throwsException().withMessage("Timed out after " + TIMEOUT + " milliseconds while sleeping")
			.withCause(InterruptedException.class);
	}

	@Test
	public void shouldThrowKalibroExceptionWrappingOtherErrorWithTimeout() {
		assertThat(new VoidTask() {

			@Override
			public void perform() throws Throwable {
				new TaskExecutor(new ThrowErrorTask(new Throwable())).executeAndWait(TIMEOUT);
			}
		}).throwsException().withMessage("Error while throwing error").withCause(Throwable.class);
	}

	@Test
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