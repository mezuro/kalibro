package org.kalibro.core.concurrent;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.tests.UtilityClassTest;

public class TaskExecutorTest extends UtilityClassTest {

	private static final String ANSWER = "42";
	private static final Throwable ERROR = new Throwable();

	private ThrowErrorTask errorTask;
	private DoNothingTask doNothingTask;
	private RetrieveResultTask<String> answerTask;

	@Before
	public void setUp() {
		errorTask = new ThrowErrorTask(ERROR);
		doNothingTask = new DoNothingTask();
		answerTask = new RetrieveResultTask<String>(ANSWER);
	}

	@Override
	protected Class<?> utilityClass() {
		return TaskExecutor.class;
	}

	@Test
	public void shouldGetReportForTaskDone() throws InterruptedException {
		TaskReport<?> report = executeAndGetReport(answerTask);
		assertSame(answerTask, report.getTask());
		assertTrue(report.isTaskDone());
		assertEquals(ANSWER, report.getResult());
	}

	@Test
	public void shouldGetReportForVoidTaskDone() throws InterruptedException {
		TaskReport<?> report = executeAndGetReport(doNothingTask);
		assertSame(doNothingTask, report.getTask());
		assertTrue(report.isTaskDone());
		assertNull(report.getResult());
	}

	@Test
	public void shouldGetReportForTaskNotDone() throws InterruptedException {
		TaskReport<?> report = executeAndGetReport(errorTask);
		assertSame(errorTask, report.getTask());
		assertFalse(report.isTaskDone());
		assertSame(ERROR, report.getError());
	}

	private synchronized <T> TaskReport<T> executeAndGetReport(Task<T> task) throws InterruptedException {
		return new TaskRunner<T>().executeAndGetReport(task);
	}

	@Test
	public void shouldRetrieveResultWhenExecutingNormally() {
		assertNull(TaskExecutor.execute(doNothingTask));
		assertEquals(ANSWER, TaskExecutor.execute(answerTask));
	}

	@Test
	public void shouldThrowExceptionWrappingTaskError() {
		assertThat(executionOf(errorTask)).throwsException().withCause(ERROR)
			.withMessage("Error while throwing error.");
	}

	@Test
	public void shouldThrowErrorForExecutionException() {
		assertThat(executionOf(new BadTask())).throwsError().withCause(ExecutionException.class)
			.withMessage("Error while overriding run()\nTask.run() should not be overriden.");
	}

	@Test
	public void shouldExecuteWithTimeout() {
		assertEquals(ANSWER, TaskExecutor.execute(answerTask, 100, MILLISECONDS));
		assertThat(executionOf(new SleepTask(100))).throwsException().withCause(TimeoutException.class)
			.withMessage("Timed out after 50 milliseconds while sleeping for 100 milliseconds.");
	}

	@Test
	public void shouldThrowExceptionForInterruption() {
		final Thread thread = Thread.currentThread();
		new Thread(new VoidTask() {

			@Override
			protected void perform() throws InterruptedException {
				Thread.sleep(25);
				thread.interrupt();
			}
		}).start();
		assertThat(executionOf(new SleepTask(40))).throwsException().withCause(InterruptedException.class)
			.withMessage("Thread interrupted while waiting for task to finish: sleeping for 40 milliseconds.");
	}

	private VoidTask executionOf(final Task<?> task) {
		return new VoidTask() {

			@Override
			protected void perform() {
				TaskExecutor.execute(task, 50, MILLISECONDS);
			}
		};
	}

	@Test
	public void shouldExecutePeriodically() throws InterruptedException {
		long period = 100;
		CounterTask task = new CounterTask();
		task.executePeriodically(period, MILLISECONDS);

		Thread.sleep(period / 2);
		assertEquals(1, task.result);

		Thread.sleep(period);
		assertEquals(2, task.result);

		Thread.sleep(period);
		assertEquals(3, task.result);
		task.cancelExecution();

		Thread.sleep(period);
		assertEquals(3, task.result);
	}

	private class TaskRunner<T> implements TaskListener<T> {

		private boolean waiting;

		private synchronized TaskReport<T> executeAndGetReport(Task<T> task) throws InterruptedException {
			task.addListener(this);
			TaskExecutor.executeInBackground(task);
			waitReport();
			return task.getReport();
		}

		private void waitReport() throws InterruptedException {
			waiting = true;
			while (waiting)
				wait();
		}

		@Override
		public synchronized void taskFinished(TaskReport<T> taskReport) {
			waiting = false;
			notify();
		}
	}
}