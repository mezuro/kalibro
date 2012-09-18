package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Task.class)
public class TaskTest extends TestCase implements TaskListener {

	private TaskReport<?> report;
	private TaskExecutor executor;

	@Before
	public void setUp() throws Exception {
		executor = PowerMockito.mock(TaskExecutor.class);
		PowerMockito.whenNew(TaskExecutor.class).withArguments(any()).thenReturn(executor);
	}

	@Test
	public void shouldExecuteInBackground() {
		new DoNothingTask().executeInBackground();
		verify(executor).executeInBackground();
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		new DoNothingTask().executeAndWait();
		verify(executor).executeAndWait();
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		new DoNothingTask().executeAndWait(42);
		verify(executor).executeAndWait(42);
	}

	@Test
	public void shouldExecutePeriodically() {
		new DoNothingTask().executePeriodically(42);
		Mockito.verify(executor).executePeriodically(42);
	}

	@Test
	public void shouldCancelPeriodicExecution() {
		new DoNothingTask().cancelPeriodicExecution();
		verify(executor).cancelPeriodicExecution();
	}

	@Test
	public void shouldNotifyListenerOfTaskDone() throws InterruptedException {
		runAndGetReport(new DoNothingTask());
		assertTrue(report.isTaskDone());
		assertNull(report.getError());
	}

	@Test
	public void shouldNotifyListenerOfTaskHalted() throws InterruptedException {
		runAndGetReport(new ThrowErrorTask());
		assertFalse(report.isTaskDone());
		assertNotNull(report.getError());
	}

	@Test
	public void shouldRetrieveReport() throws InterruptedException {
		Task task = new DoNothingTask();
		runAndGetReport(task);
		assertSame(report, task.getReport());
	}

	@Test
	public void shouldHaveDefaultDescription() {
		assertEquals("running task: org.kalibro.core.concurrent.DoNothingTask", "" + new DoNothingTask());
	}

	private synchronized void runAndGetReport(Task task) throws InterruptedException {
		report = null;
		task.setListener(this);
		new Thread(task).start();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport<?> taskReport) {
		report = taskReport;
		notifyTest();
	}
}