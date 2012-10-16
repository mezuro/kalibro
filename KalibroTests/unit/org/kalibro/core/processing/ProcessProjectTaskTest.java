package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.ModuleResultFixtures.newHelloWorldResults;
import static org.kalibro.ProjectFixtures.*;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.*;
import org.kalibro.core.persistence.ModuleResultDatabaseDao;
import org.kalibro.dao.DaoFactory;
import org.kalibro.dao.ProjectDao;
import org.kalibro.dao.ProcessingDao;
import org.kalibro.tests.UnitTest;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DaoFactory.class, ProcessTask.class})
public class ProcessProjectTaskTest extends UnitTest {

	private Project project;
	private Processing processing;
	private Collection<ModuleResult> moduleResults;

	private ProjectDao projectDao;
	private ModuleResultDatabaseDao moduleResultDao;
	private ProcessingDao processingDao;

	private LoadSourceTask loadTask;
	private CollectMetricsTask collectTask;
	private AnalyzeResultsTask analyzeTask;

	private ProcessTask processTask;

	@Before
	public void setUp() throws Exception {
		project = newHelloWorld();
		project.setState(ProcessState.NEW);
		moduleResults = newHelloWorldResults();
		mockKalibro();
		mockSubtasks();
		processTask = new ProcessTask(PROJECT_NAME);
	}

	private void mockKalibro() {
		projectDao = mock(ProjectDao.class);
		moduleResultDao = mock(ModuleResultDatabaseDao.class);
		processingDao = mock(ProcessingDao.class);
		mockStatic(DaoFactory.class);
		when(DaoFactory.getProjectDao()).thenReturn(projectDao);
		when(DaoFactory.getModuleResultDao()).thenReturn(moduleResultDao);
		when(DaoFactory.getProjectResultDao()).thenReturn(processingDao);
		when(projectDao.getProject(PROJECT_NAME)).thenReturn(project);
	}

	private void mockSubtasks() throws Exception {
		loadTask = mock(LoadSourceTask.class);
		collectTask = mock(CollectMetricsTask.class);
		analyzeTask = mock(AnalyzeResultsTask.class);
		processing = mock(Processing.class);
		Map<Module, ModuleResult> resultMap = mock(Map.class);
		whenNew(LoadSourceTask.class).withArguments(project).thenReturn(loadTask);
		when(loadTask.executeSubTask()).thenReturn(processing);
		whenNew(CollectMetricsTask.class).withArguments(processing).thenReturn(collectTask);
		when(collectTask.executeSubTask()).thenReturn(resultMap);
		whenNew(AnalyzeResultsTask.class).withArguments(processing, resultMap).thenReturn(analyzeTask);
		when(analyzeTask.executeSubTask()).thenReturn(moduleResults);
	}

	@Test
	public void shouldExecuteSubtasks() {
		processTask.perform();

		InOrder order = Mockito.inOrder(loadTask, collectTask, analyzeTask);
		order.verify(loadTask).executeSubTask();
		order.verify(collectTask).executeSubTask();
		order.verify(analyzeTask).executeSubTask();
	}

	@Test
	public void shouldSaveProjectWithUpdatedState() {
		processTask.perform();
		assertEquals(ProcessState.READY, project.getState());
		Mockito.verify(projectDao).save(project);
	}

	@Test
	public void shouldSaveResults() {
		processTask.perform();

		InOrder order = Mockito.inOrder(processingDao, moduleResultDao);
		order.verify(processingDao).save(processing);
		for (ModuleResult moduleResult : moduleResults)
			order.verify(moduleResultDao).save(moduleResult, processing);
	}

	@Test
	public void shouldSaveProjectWithError() {
		RuntimeException error = mock(RuntimeException.class);
		when(loadTask.executeSubTask()).thenThrow(error);

		processTask.perform();
		assertEquals(ProcessState.ERROR, project.getState());
		assertSame(error, project.getError());
		Mockito.verify(projectDao).save(project);
	}
}