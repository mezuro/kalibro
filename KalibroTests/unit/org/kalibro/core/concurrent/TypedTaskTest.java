package org.kalibro.core.concurrent;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Task.class)
public class TypedTaskTest extends KalibroTestCase implements TaskListener {

	private TaskExecutor taskExecutor;
	private TypedTaskReport<String> taskReport;

	@Before
	public void setUp() throws Exception {
		taskExecutor = PowerMockito.mock(TaskExecutor.class);
		PowerMockito.whenNew(TaskExecutor.class).withArguments(any()).thenReturn(taskExecutor);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteWithTimeout() {
		final SetResultTask<String> task = new SetResultTask<String>("42");
		PowerMockito.doAnswer(new Answer<Object>() {

			@Override
			public Object answer(InvocationOnMock invocation) throws Throwable {
				task.perform();
				return null;
			}
		}).when(taskExecutor).executeAndWait(84);
		assertEquals("42", task.executeAndWaitResult(84));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testTaskDone() throws InterruptedException {
		SetResultTask<String> task = new SetResultTask<String>("The result");
		runAndGetReport(task);
		assertTrue(taskReport.isTaskDone());
		assertEquals("The result", taskReport.getResult());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void testTaskHalted() throws InterruptedException {
		runAndGetReport(new ThrowExceptionTypedTask<String>());
		assertFalse(taskReport.isTaskDone());
		assertNotNull(taskReport.getError());
	}

	private synchronized void runAndGetReport(TypedTask<String> task) throws InterruptedException {
		task.setListener(this);
		new Thread(task).start();
		waitNotification();
	}

	@Override
	public synchronized void taskFinished(TaskReport report) {
		taskReport = (TypedTaskReport<String>) report;
		notifyTest();
	}
}