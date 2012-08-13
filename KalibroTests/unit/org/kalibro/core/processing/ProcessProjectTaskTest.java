package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Module;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.kalibro.core.persistence.database.ModuleResultDatabaseDao;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Kalibro.class, ProcessProjectTask.class})
public class ProcessProjectTaskTest extends KalibroTestCase {

	private Project project;
	private ProjectResult projectResult;
	private Collection<ModuleResult> moduleResults;

	private ProjectDao projectDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private ProjectResultDao projectResultDao;

	private LoadSourceTask loadTask;
	private CollectMetricsTask collectTask;
	private AnalyzeResultsTask analyzeTask;

	private ProcessProjectTask processTask;

	@Before
	public void setUp() throws Exception {
		project = newHelloWorld();
		project.setState(ProjectState.NEW);
		moduleResults = newHelloWorldResults();
		mockKalibro();
		mockSubtasks();
		processTask = new ProcessProjectTask(PROJECT_NAME);
	}

	private void mockKalibro() {
		projectDao = mock(ProjectDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		projectResultDao = mock(ProjectResultDao.class);
		mockStatic(Kalibro.class);
		when(Kalibro.getProjectDao()).thenReturn(projectDao);
		when(Kalibro.getModuleResultDao()).thenReturn(moduleResultDao);
		when(Kalibro.getProjectResultDao()).thenReturn(projectResultDao);
		when(projectDao.getProject(PROJECT_NAME)).thenReturn(project);
	}

	private void mockSubtasks() throws Exception {
		loadTask = mock(LoadSourceTask.class);
		collectTask = mock(CollectMetricsTask.class);
		analyzeTask = mock(AnalyzeResultsTask.class);
		projectResult = mock(ProjectResult.class);
		Map<Module, ModuleResult> resultMap = mock(Map.class);
		whenNew(LoadSourceTask.class).withArguments(project).thenReturn(loadTask);
		when(loadTask.execute()).thenReturn(projectResult);
		whenNew(CollectMetricsTask.class).withArguments(projectResult).thenReturn(collectTask);
		when(collectTask.execute()).thenReturn(resultMap);
		whenNew(AnalyzeResultsTask.class).withArguments(projectResult, resultMap).thenReturn(analyzeTask);
		when(analyzeTask.execute()).thenReturn(moduleResults);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteSubtasks() {
		processTask.perform();

		InOrder order = Mockito.inOrder(loadTask, collectTask, analyzeTask);
		order.verify(loadTask).execute();
		order.verify(collectTask).execute();
		order.verify(analyzeTask).execute();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveProjectWithUpdatedState() {
		processTask.perform();
		assertEquals(ProjectState.READY, project.getState());
		Mockito.verify(projectDao).save(project);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveResults() {
		processTask.perform();

		InOrder order = Mockito.inOrder(projectResultDao, moduleResultDao);
		order.verify(projectResultDao).save(projectResult);
		for (ModuleResult moduleResult : moduleResults)
			order.verify(moduleResultDao).save(moduleResult, projectResult);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveProjectWithError() {
		RuntimeException error = mock(RuntimeException.class);
		when(loadTask.execute()).thenThrow(error);

		processTask.perform();
		assertEquals(ProjectState.ERROR, project.getState());
		assertSame(error, project.getError());
		Mockito.verify(projectDao).save(project);
	}
}