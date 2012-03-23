package org.kalibro.core.processing;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, ProcessProjectTask.class})
public class ProcessProjectTaskTest extends KalibroTestCase {

	private Project project;
	private ProcessProjectTask processTask;
	private LoadProjectExecutor loadExecutor;

	@Before
	public void setUp() throws Exception {
		mockProject();
		mockLoadProjectExecutor();
		processTask = new ProcessProjectTask("Sample from ProcessProjectTaskTest");
	}

	private void mockProject() {
		project = PowerMockito.mock(Project.class);
		ProjectDao projectDao = PowerMockito.mock(ProjectDao.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getProjectDao()).thenReturn(projectDao);
		PowerMockito.when(projectDao.getProject("Sample from ProcessProjectTaskTest")).thenReturn(project);
	}

	private void mockLoadProjectExecutor() throws Exception {
		loadExecutor = PowerMockito.mock(LoadProjectExecutor.class);
		PowerMockito.whenNew(LoadProjectExecutor.class).withArguments(project).thenReturn(loadExecutor);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldRunLoadProjectExecutor() {
		processTask.perform();
		Mockito.verify(loadExecutor).execute();
	}
}