package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ModuleNodeFixtures.*;
import static org.kalibro.core.model.ModuleResultFixtures.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;
import static org.powermock.api.mockito.PowerMockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class AnalyzeResultsTaskTest extends KalibroTestCase {

	private ProjectResult projectResult;
	private ModuleResultDao moduleResultDao;
	private ProjectResultDao projectResultDao;

	private AnalyzeResultsTask analyzeTask;

	@Before
	public void setUp() {
		projectResult = newHelloWorldResult();
		projectResult.setSourceTree(null);
		mockKalibro();
		analyzeTask = new AnalyzeResultsTask(projectResult, newHelloWorldResultMap(projectResult.getDate()));
	}

	private void mockKalibro() {
		moduleResultDao = mock(ModuleResultDao.class);
		projectResultDao = mock(ProjectResultDao.class);
		mockStatic(Kalibro.class);
		when(Kalibro.getModuleResultDao()).thenReturn(moduleResultDao);
		when(Kalibro.getProjectResultDao()).thenReturn(projectResultDao);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.ANALYZING, analyzeTask.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetSourceTreeOnProjectResult() {
		analyzeTask.performAndGetResult();
		assertDeepEquals(helloWorldRoot(), projectResult.getSourceTree());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSaveResults() {
		analyzeTask.performAndGetResult();
		String projectName = projectResult.getProject().getName();

		InOrder order = Mockito.inOrder(projectResultDao, moduleResultDao);
		order.verify(projectResultDao).save(projectResult);
		for (ModuleResult moduleResult : newHelloWorldResults(projectResult.getDate()))
			order.verify(moduleResultDao).save(moduleResult, projectName);
	}
}