package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.kalibro.TestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.mockito.Mockito;

public class LoadSourceTaskTest extends TestCase {

	private Project project;

	private LoadSourceTask loadTask;

	@Before
	public void setUp() {
		project = mock(Project.class);
		loadTask = new LoadSourceTask(project);
	}

	@Test
	public void checkTaskState() {
		assertEquals(ProjectState.LOADING, loadTask.getTaskState());
	}

	@Test
	public void shouldLoadProject() {
		loadTask.compute();
		Mockito.verify(project).load();
	}

	@Test
	public void shouldReturnProjectResult() {
		assertSame(loadTask.projectResult, loadTask.compute());
	}
}