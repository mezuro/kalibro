package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.AnswerAdapter;
import org.kalibro.TestCase;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TaskExecutor.class)
public class TaskTest extends TestCase implements TaskListener<Void> {

	private VoidTask task;
	private TaskReport<?> report;

	@Before
	public void setUp() {
		mockStatic(TaskExecutor.class);
		when(TaskExecutor.execute(task)).thenAnswer(new SetReport());
		when(TaskExecutor.execute(eq(task), anyLong(), eq(TimeUnit.MILLISECONDS))).thenAnswer(new SetReport());
		task = new DoNothingTask();
	}

	@Test
	public void shouldExecuteInBackground() {
		task.executeInBackground();
		verifyStatic();
		TaskExecutor.executeInBackground(task);
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		task.executeAndWait();
		verifyStatic();
		TaskExecutor.execute(task);
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		task.executeAndWait(42);
		verifyStatic();
		TaskExecutor.execute(task, 42, TimeUnit.MILLISECONDS);
	}

	@Test
	public void shouldExecutePeriodically() {
		task.executePeriodically(42);
		verifyStatic();
		TaskExecutor.executePeriodically(task, 42, TimeUnit.MILLISECONDS);
	}

	@Test
	public void shouldCancelPeriodicExecution() {
		Future future = mock(Future.class);
		when(TaskExecutor.executePeriodically(task, 42, TimeUnit.MILLISECONDS)).thenReturn(future);

		task.executePeriodically(42);
		task.cancelPeriodicExecution();
		verify(future).cancel(false);
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