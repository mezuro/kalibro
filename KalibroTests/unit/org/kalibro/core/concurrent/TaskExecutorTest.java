package org.kalibro.core.concurrent;

import static org.junit.Assert.*;

import org.junit.Test;
import org.kalibro.KalibroTestCase;

public class TaskExecutorTest extends KalibroTestCase implements TaskListener {

	private TaskReport taskReport;

	@Test(timeout = UNIT_TIMEOUT)
	public void testNormalExecution() {
		SetResultTask<String> task = new SetResultTask<String>("The result");
		assertNull(task.result);
		new TaskExecutor(task).executeAndWait(100);
		assertEquals("The result", task.result);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testExecutionError() {
		checkException(new Task() {

			@Override
			public void perform() {
				new TaskExecutor(new ThrowExceptionTask()).executeAndWait(100);
			}
		}, RuntimeException.class, "Task did not end normally.", Exception.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testTimeoutError() {
		checkException(new Task() {

			@Override
			public void perform() {
				new TaskExecutor(new SleepTask(200)).executeAndWait(100);
			}
		}, RuntimeException.class, "Task timed out after 100 milliseconds.", InterruptedException.class);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testNormalExecutionInBackground() throws InterruptedException {
		executeAndGetReport(new DoNothingTask());
		assertTrue(taskReport.isTaskDone());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testExecutionErrorInBackground() throws InterruptedException {
		executeAndGetReport(new ThrowExceptionTask());
		assertFalse(taskReport.isTaskDone());
	}

	private synchronized void executeAndGetReport(Task task) throws InterruptedException {
		task.setListener(this);
		new TaskExecutor(task).executeInBackground();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport report) {
		taskReport = report;
		notifyTest();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testPeriodicExecution() throws InterruptedException {
		IncrementResultTask task = new IncrementResultTask();
		TaskExecutor executor = new TaskExecutor(task);

		long period = 50;
		executor.executePeriodically(period);

		Thread.sleep(period / 2);
		assertEquals(1, task.result.intValue());

		Thread.sleep(period);
		assertEquals(2, task.result.intValue());

		Thread.sleep(period);
		assertEquals(3, task.result.intValue());

		executor.cancelPeriodicExecution();
		Thread.sleep(period);
		assertEquals(3, task.result.intValue());
	}

	private class IncrementResultTask extends TypedTask<Integer> {

		public IncrementResultTask() {
			setResult(0);
		}

		@Override
		public Integer generateResult() {
			return result + 1;
		}
	}
}