package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Task.class)
public class TypedTaskTest extends TestCase implements TaskListener {

	private static final String RESULT = "TypedTaskTest result";

	private TypedTaskReport<String> report;
	private TaskExecutor executor;

	@Before
	public void setUp() throws Exception {
		executor = PowerMockito.mock(TaskExecutor.class);
		PowerMockito.whenNew(TaskExecutor.class).withArguments(any()).thenReturn(executor);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteAndWaitWithoutTimeout() throws Throwable {
		RetrieveResultTask<String> task = new RetrieveResultTask<String>(RESULT);
		task.perform();

		assertEquals(RESULT, task.executeAndWaitResult());
		verify(executor).executeAndWait();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteAndWaitWithTimeout() throws Throwable {
		long timeout = 42;
		RetrieveResultTask<String> task = new RetrieveResultTask<String>(RESULT);
		task.perform();

		assertEquals(RESULT, task.executeAndWaitResult(timeout));
		verify(executor).executeAndWait(timeout);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyListenerOfTaskDone() throws InterruptedException {
		RetrieveResultTask<String> task = new RetrieveResultTask<String>(RESULT);
		runAndGetReport(task);
		assertTrue(report.isTaskDone());
		assertEquals(RESULT, report.getResult());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldNotifyListenerOfTaskHalted() throws InterruptedException {
		runAndGetReport(new ThrowExceptionTypedTask<String>());
		assertFalse(report.isTaskDone());
		assertNotNull(report.getError());
	}

	private synchronized void runAndGetReport(TypedTask<String> task) throws InterruptedException {
		report = null;
		task.setListener(this);
		new Thread(task).start();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport taskReport) {
		report = (TypedTaskReport<String>) taskReport;
		notifyTest();
	}
}