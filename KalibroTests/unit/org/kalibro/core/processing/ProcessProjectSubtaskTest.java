package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.kalibro.core.model.enums.ProjectState.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class ProcessProjectSubtaskTest extends KalibroTestCase {

	private static final String RESULT = "ProcessProjectSubtaskTest result";

	private Project project;
	private ProjectDao projectDao;

	private FakeSubtask subtask;

	@Before
	public void setUp() {
		project = newHelloWorld();
		project.setState(NEW);
		mockKalibro();
		subtask = new FakeSubtask(project);
	}

	private void mockKalibro() {
		projectDao = mock(ProjectDao.class);
		mockStatic(Kalibro.class);
		when(Kalibro.getProjectDao()).thenReturn(projectDao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnResult() {
		assertEquals(RESULT, subtask.execute());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldUpdateProjectState() {
		subtask.execute();
		assertEquals(subtask.getTaskState(), project.getState());
		Mockito.verify(projectDao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetStateTime() {
		subtask.execute();
		Mockito.verify(subtask.projectResult).setStateTime(eq(subtask.getTaskState()), anyLong());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldDescribeTaskWithProjectStateMessage() {
		assertEquals(project.getStateMessage(), "" + subtask);
	}

	private class FakeSubtask extends ProcessProjectSubtask<String> {

		public FakeSubtask(Project project) {
			super(spy(new ProjectResult(project)));
		}

		@Override
		protected ProjectState getTaskState() {
			return COLLECTING;
		}

		@Override
		protected String performAndGetResult() {
			return RESULT;
		}
	}
}