package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Task.class)
public class TaskTest extends KalibroTestCase implements TaskListener {

	private TaskExecutor taskExecutor;
	private TaskReport taskReport;

	@Before
	public void setUp() throws Exception {
		taskExecutor = PowerMockito.mock(TaskExecutor.class);
		PowerMockito.whenNew(TaskExecutor.class).withArguments(any()).thenReturn(taskExecutor);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteInBackground() {
		new DoNothingTask().executeInBackground();
		Mockito.verify(taskExecutor).executeInBackground();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteWithTimeout() {
		new DoNothingTask().executeAndWait(42);
		Mockito.verify(taskExecutor).executeAndWait(42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecutePeriodically() {
		new DoNothingTask().executePeriodically(42);
		Mockito.verify(taskExecutor).executePeriodically(42);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCancelPeriodicExecution() {
		new DoNothingTask().cancelPeriodicExecution();
		Mockito.verify(taskExecutor).cancelPeriodicExecution();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testTaskDone() throws InterruptedException {
		runAndGetReport(new DoNothingTask());
		assertTrue(taskReport.isTaskDone());
		assertNull(taskReport.getError());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testTaskHalted() throws InterruptedException {
		runAndGetReport(new ThrowExceptionTask());
		assertFalse(taskReport.isTaskDone());
		assertNotNull(taskReport.getError());
	}

	private synchronized void runAndGetReport(Task task) throws InterruptedException {
		task.setListener(this);
		new Thread(task).start();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport report) {
		taskReport = report;
		notifyTest();
	}
}