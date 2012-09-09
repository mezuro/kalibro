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

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.LOADING, loadTask.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadProject() {
		loadTask.performAndGetResult();
		Mockito.verify(project).load();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnProjectResult() {
		assertSame(loadTask.projectResult, loadTask.performAndGetResult());
	}
}