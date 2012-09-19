package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.AnswerAdapter;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Task.class)
public class TaskTest extends TestCase implements TaskListener<Void> {

	private TaskReport<?> report;
	private TaskExecutor executor;

	private VoidTask task;

	@Before
	public void setUp() throws Exception {
		executor = mock(TaskExecutor.class);
		whenNew(TaskExecutor.class).withArguments(any()).thenReturn(executor);
		doAnswer(new SetReport()).when(executor).executeAndWait();
		doAnswer(new SetReport()).when(executor).executeAndWait(anyLong());
		task = new DoNothingTask();
	}

	@Test
	public void shouldExecuteInBackground() {
		task.executeInBackground();
		verify(executor).executeInBackground();
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		task.executeAndWait();
		verify(executor).executeAndWait();
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		task.executeAndWait(42);
		verify(executor).executeAndWait(42);
	}

	@Test
	public void shouldExecutePeriodically() {
		task.executePeriodically(42);
		verify(executor).executePeriodically(42);
	}

	@Test
	public void shouldCancelPeriodicExecution() {
		task.cancelPeriodicExecution();
		verify(executor).cancelPeriodicExecution();
	}

	@Test
	public void shouldNotifyListenerOfTaskDone() throws InterruptedException {
		runAndGetReport();
		assertTrue(report.isTaskDone());
		assertNull(report.getError());
	}

	@Test
	public void shouldNotifyListenerOfTaskHalted() throws InterruptedException {
		task = new ThrowErrorTask();
		runAndGetReport();
		assertFalse(report.isTaskDone());
		assertNotNull(report.getError());
	}

	@Test
	public void shouldRetrieveReport() throws InterruptedException {
		runAndGetReport();
		assertSame(report, task.getReport());
	}

	@Test
	public void shouldHaveDefaultDescription() {
		assertEquals("running task: org.kalibro.core.concurrent.DoNothingTask", "" + task);
	}

	private synchronized void runAndGetReport() throws InterruptedException {
		report = null;
		task.addListener(this);
		new Thread(task).start();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport<Void> taskReport) {
		report = taskReport;
		notifyTest();
	}

	private class SetReport extends AnswerAdapter {

		@Override
		protected void answer() throws Throwable {
			task.setReport(new TaskReport<Void>(task, 0, (Void) null));
		}
	}
}