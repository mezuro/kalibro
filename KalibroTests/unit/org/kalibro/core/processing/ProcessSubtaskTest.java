package org.kalibro.core.processing;

import static org.junit.Assert.assertEquals;
import static org.kalibro.ProcessState.COLLECTING;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Project;
import org.kalibro.Processing;
import org.kalibro.ProcessState;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.tests.UnitTest;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DaoFactory.class)
public class ProcessSubtaskTest extends UnitTest {

	private static final String TASK_RESULT = "ProcessSubtaskTest result";
	private static final ProcessState TASK_STATE = COLLECTING;

	private Project project;
	private ProjectDao projectDao;
	private Processing processing;

	private FakeSubtask subtask;

	@Before
	public void setUp() {
		mockDaoFactory();
		mockProjectResult();
		subtask = new FakeSubtask(processing);
	}

	private void mockDaoFactory() {
		projectDao = mock(ProjectDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(projectDao);
	}

	private void mockProjectResult() {
		project = mock(Project.class);
		processing = mock(Processing.class);
		when(processing.getRepository()).thenReturn(project);
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
		Mockito.verify(processing).setStateTime(eq(TASK_STATE), anyLong());
	}

	@Test
	public void shouldDescribeTaskWithProjectStateMessage() {
		when(project.getStateMessage()).thenReturn(TASK_RESULT);
		assertEquals(TASK_RESULT, "" + subtask);
	}

	private final class FakeSubtask extends ProcessSubtask<String> {

		private FakeSubtask(Processing result) {
			super(result);
		}

		@Override
		protected ProcessState getTaskState() {
			return TASK_STATE;
		}

		@Override
		protected String compute() {
			return TASK_RESULT;
		}
	}
}