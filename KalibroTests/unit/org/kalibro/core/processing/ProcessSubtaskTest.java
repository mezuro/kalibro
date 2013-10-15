package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.ProcessState;
import org.kalibro.Processing;
import org.kalibro.Repository;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProcessSubtask.class)
public class ProcessSubtaskTest extends UnitTest {

	private ProcessContext context;

	private ProcessSubtask subtask;

	@Before
	public void setUp() {
		context = new ProcessContext(mock(Repository.class));
		context.processing = mock(Processing.class);
		subtask = new ReadyTask(context);
	}

	@Test
	public void shouldRetrieveTaskState() {
		assertEquals(ProcessState.READY, subtask.getState());
	}

	@Test
	public void toStringShouldBeStateMessage() {
		String repositoryName = "ProcessingTest repository complete name";
		when(context.repository.getCompleteName()).thenReturn(repositoryName);
		for (ProcessState state : ProcessState.values()) {
			when(context.processing.getState()).thenReturn(state);
			assertEquals(state.getMessage(repositoryName), "" + subtask);
		}
	}
}