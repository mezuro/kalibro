package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.enums.ProjectState.COLLECTING;
import static org.mockito.Matchers.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.TestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProcessProjectSubtaskTest extends TestCase {

	private static final String TASK_RESULT = "ProcessProjectSubtaskTest result";
	private static final ProjectState TASK_STATE = COLLECTING;

	private Project project;
	private ProjectDao projectDao;
	private ProjectResult projectResult;

	private FakeSubtask subtask;

	@Before
	public void setUp() {
		mockDaoFactory();
		mockProjectResult();
		subtask = new FakeSubtask(projectResult);
	}

	private void mockDaoFactory() {
		projectDao = mock(ProjectDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(projectDao);
	}

	private void mockProjectResult() {
		project = mock(Project.class);
		projectResult = mock(ProjectResult.class);
		when(projectResult.getProject()).thenReturn(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnTaskResult() {
		assertEquals(TASK_RESULT, subtask.execute());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUpdateProjectState() {
		subtask.execute();
		Mockito.verify(project).setState(subtask.getTaskState());
		Mockito.verify(projectDao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetStateTime() {
		subtask.execute();
		Mockito.verify(projectResult).setStateTime(eq(TASK_STATE), anyLong());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDescribeTaskWithProjectStateMessage() {
		when(project.getStateMessage()).thenReturn(TASK_RESULT);
		assertEquals(TASK_RESULT, "" + subtask);
	}

	private final class FakeSubtask extends ProcessProjectSubtask<String> {

		private FakeSubtask(ProjectResult result) {
			super(result);
		}

		@Override
		protected ProjectState getTaskState() {
			return TASK_STATE;
		}

		@Override
		protected String performAndGetResult() {
			return TASK_RESULT;
		}
	}
}