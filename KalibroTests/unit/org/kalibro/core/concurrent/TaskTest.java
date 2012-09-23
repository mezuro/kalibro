package org.kalibro.core.concurrent;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

import java.util.concurrent.Future;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Task.class, TaskExecutor.class})
public class TaskTest extends UnitTest {

	private static final String ANSWER = "42";

	@SuppressWarnings("rawtypes")
	private Future future;
	private RetrieveResultTask<String> task;

	@Before
	public void setUp() {
		future = mock(Future.class);
		mockStatic(TaskExecutor.class);
		task = new RetrieveResultTask<String>(ANSWER);
	}

	@Test
	public void shouldExecuteInBackground() {
		task.executeInBackground();
		verifyStatic();
		TaskExecutor.executeInBackground(task);
	}

	@Test
	public void shouldExecute() {
		when(TaskExecutor.execute(task)).thenReturn(ANSWER);
		assertEquals(ANSWER, task.execute());
	}

	@Test
	public void shouldExecuteWithTimeout() {
		when(TaskExecutor.execute(task, 42, DAYS)).thenReturn(ANSWER);
		assertEquals(ANSWER, task.execute(42, DAYS));
	}

	@Test
	public void shouldExecutePeriodically() {
		task.executePeriodically(42, HOURS);
		verifyStatic();
		TaskExecutor.executePeriodically(task, 42, HOURS);
	}

	@Test
	public void shouldCancelBackgroundExecution() {
		when(TaskExecutor.executeInBackground(task)).thenReturn(future);
		task.executeInBackground();
		task.cancelExecution();
		verify(future).cancel(true);
	}

	@Test
	public void shouldCancelPeriodicExecution() {
		when(TaskExecutor.executePeriodically(task, 42, MINUTES)).thenReturn(future);
		task.executePeriodically(42, MINUTES);
		task.cancelExecution();
		verify(future).cancel(true);
	}

	@Test
	public void shouldComputeReportForTaskDone() {
		assertNull(task.getReport());
		task.run();

		TaskReport<String> report = task.getReport();
		assertSame(task, report.getTask());
		assertTrue(report.isTaskDone());
		assertEquals(ANSWER, report.getResult());
	}

	@Test
	public void shouldComputeReportForTaskNotDone() {
		Throwable error = new Throwable();
		ThrowErrorTask errorTask = new ThrowErrorTask(error);
		assertNull(errorTask.getReport());
		errorTask.run();

		TaskReport<?> report = errorTask.getReport();
		assertSame(errorTask, report.getTask());
		assertFalse(report.isTaskDone());
		assertSame(error, report.getError());
	}

	@Test
	public void shouldNotifyListenersInBackground() throws Exception {
		TaskListener<String> listener = mock(TaskListener.class);
		TaskListenerNotifier<String> notifier = mock(TaskListenerNotifier.class);
		whenNew(TaskListenerNotifier.class).withArguments(any(TaskReport.class), same(listener)).thenReturn(notifier);

		task.addListener(listener);
		task.run();
		verify(notifier).executeInBackground();
	}

	@Test
	public void shouldHaveDefaultDescription() {
		assertEquals("running task: org.kalibro.core.concurrent.DoNothingTask", "" + new DoNothingTask());
	}
}