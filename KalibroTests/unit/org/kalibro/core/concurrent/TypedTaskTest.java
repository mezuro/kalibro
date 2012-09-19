package org.kalibro.core.concurrent;

import static org.junit.Assert.assertEquals;
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
public class TypedTaskTest extends TestCase {

	private static final String RESULT = "TypedTaskTest result";

	private TaskExecutor executor;
	private RetrieveResultTask<String> task;

	@Before
	public void setUp() throws Exception {
		executor = mock(TaskExecutor.class);
		whenNew(TaskExecutor.class).withArguments(any()).thenReturn(executor);
		doAnswer(new SetReport()).when(executor).executeAndWait();
		doAnswer(new SetReport()).when(executor).executeAndWait(anyLong());
	}

	@Test
	public void shouldExecuteAndWaitWithoutTimeout() {
		task = new RetrieveResultTask<String>(RESULT);
		task.compute();

		assertEquals(RESULT, task.executeAndWaitResult());
		verify(executor).executeAndWait();
	}

	@Test
	public void shouldExecuteAndWaitWithTimeout() {
		long timeout = 42;
		task = new RetrieveResultTask<String>(RESULT);
		task.compute();

		assertEquals(RESULT, task.executeAndWaitResult(timeout));
		verify(executor).executeAndWait(timeout);
	}

	private class SetReport extends AnswerAdapter {

		@Override
		protected void answer() throws Throwable {
			task.setReport(new TaskReport<String>(task, 0, RESULT));
		}
	}
}