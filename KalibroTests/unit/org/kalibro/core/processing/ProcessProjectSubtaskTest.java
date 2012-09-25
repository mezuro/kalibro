package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.core.model.enums.ProjectState.COLLECTING;
import static org.mockito.Matchers.anyLong;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProcessProjectSubtaskTest extends UnitTest {

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

	@Test
	public void shouldReturnTaskResult() {
		assertEquals(TASK_RESULT, subtask.executeSubTask());
	}

	@Test
	public void shouldUpdateProjectState() {
		subtask.executeSubTask();
		Mockito.verify(project).setState(subtask.getTaskState());
		Mockito.verify(projectDao).save(project);
	}

	@Test
	public void shouldSetStateTime() {
		subtask.executeSubTask();
		Mockito.verify(projectResult).setStateTime(eq(TASK_STATE), anyLong());
	}

	@Test
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
		protected String compute() {
			return TASK_RESULT;
		}
	}
}