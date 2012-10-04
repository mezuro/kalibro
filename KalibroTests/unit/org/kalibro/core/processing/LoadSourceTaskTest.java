package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.Project;
import org.kalibro.ResultState;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;

public class LoadSourceTaskTest extends UnitTest {

	private Project project;

	private LoadSourceTask loadTask;

	@Before
	public void setUp() {
		project = mock(Project.class);
		loadTask = new LoadSourceTask(project);
	}

	@Test
	public void checkTaskState() {
		assertEquals(ResultState.LOADING, loadTask.getTaskState());
	}

	@Test
	public void shouldLoadProject() {
		loadTask.compute();
		Mockito.verify(project).load();
	}

	@Test
	public void shouldReturnProjectResult() {
		assertSame(loadTask.repositoryResult, loadTask.compute());
	}
}