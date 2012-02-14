package org.kalibro.core.processing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectFixtures;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LoadProjectExecutor.class)
public class LoadProjectExecutorTest extends KalibroTestCase {

	private Project project;
	private LoadProjectExecutor executor;

	@Before
	public void setUp() {
		project = ProjectFixtures.helloWorld();
		executor = new LoadProjectExecutor(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.LOADING, executor.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTask() throws Exception {
		LoadProjectTask task = PowerMockito.mock(LoadProjectTask.class);
		PowerMockito.whenNew(LoadProjectTask.class).withArguments(project).thenReturn(task);

		assertSame(task, executor.getTask());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetTaskExecutionTime() {
		executor.setTaskExecutionTime(42);
		assertEquals(42L, executor.projectResult.getLoadTime().longValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkContinuation() throws Exception {
		ProjectResult projectResult = executor.projectResult;
		AnalyzeProjectExecutor analysisExecutor = PowerMockito.mock(AnalyzeProjectExecutor.class);
		PowerMockito.whenNew(AnalyzeProjectExecutor.class).withArguments(projectResult).thenReturn(analysisExecutor);

		executor.continueProcessing(null);
		Mockito.verify(analysisExecutor).execute();
	}
}