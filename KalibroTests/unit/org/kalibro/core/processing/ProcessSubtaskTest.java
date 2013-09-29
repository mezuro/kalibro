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

	private Repository repository;
	private Processing processing;
	private ProcessContext context;

	private ProcessSubtask subtask;

	@Before
	public void setUp() {
		mockContext();
		subtask = new ReadyTask(context);
	}

	private void mockContext() {
		repository = mock(Repository.class);
		processing = mock(Processing.class);
		context = mock(ProcessContext.class);
		when(context.repository()).thenReturn(repository);
		when(context.processing()).thenReturn(processing);
	}

	@Test
	public void shouldRetrieveTaskState() {
		assertEquals(ProcessState.READY, subtask.getState());
	}

	@Test
	public void toStringShouldBeStateMessage() {
		String repositoryName = "ProcessingTest repository complete name";
		when(repository.getCompleteName()).thenReturn(repositoryName);
		for (ProcessState state : ProcessState.values()) {
			when(processing.getState()).thenReturn(state);
			assertEquals(state.getMessage(repositoryName), "" + subtask);
		}
	}
}