package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectResultFixtures.*;

import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.concurrent.TypedTaskReport;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.ModuleResultFixtures;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AnalyzeProjectExecutor.class, Kalibro.class})
public class AnalyzeProjectExecutorTest extends KalibroTestCase {

	private ProjectResult projectResult;
	private AnalyzeProjectExecutor executor;

	@Before
	public void setUp() {
		projectResult = helloWorldResult();
		executor = PowerMockito.spy(new AnalyzeProjectExecutor(projectResult));
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.ANALYZING, executor.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTask() throws Exception {
		AnalyzeProjectTask task = PowerMockito.mock(AnalyzeProjectTask.class);
		PowerMockito.whenNew(AnalyzeProjectTask.class).withArguments(projectResult).thenReturn(task);

		assertSame(task, executor.getTask());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldSetTaskExecutionTime() {
		executor.setTaskExecutionTime(42);
		assertEquals(42L, projectResult.getAnalysisTime().longValue());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkContinuation() throws Exception {
		ProjectResultDao projectResultDao = PowerMockito.mock(ProjectResultDao.class);
		ModuleResultDao moduleResultDao = PowerMockito.mock(ModuleResultDao.class);
		mockKalibro(projectResultDao, moduleResultDao);
		PowerMockito.doNothing().when(executor, "updateProjectState", ProjectState.READY);
		PowerMockito.mockStatic(FileUtils.class);

		Collection<ModuleResult> moduleResults = ModuleResultFixtures.helloWorldModuleResults();
		executor.continueProcessing(new TypedTaskReport<Collection<ModuleResult>>(0, null, moduleResults));

		String projectName = projectResult.getProject().getName();
		Mockito.verify(projectResultDao).save(projectResult);
		for (ModuleResult moduleResult : moduleResults)
			Mockito.verify(moduleResultDao).save(moduleResult, projectName, projectResult.getDate());
		PowerMockito.verifyPrivate(executor).invoke("updateProjectState", ProjectState.READY);
		PowerMockito.verifyStatic();
		FileUtils.deleteQuietly(HELLO_WORLD_DIRECTORY);
	}

	private void mockKalibro(ProjectResultDao projectResultDao, ModuleResultDao moduleResultDao) {
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.getProjectResultDao()).thenReturn(projectResultDao);
		PowerMockito.when(Kalibro.getModuleResultDao()).thenReturn(moduleResultDao);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(getTestSettings());
	}

	private KalibroSettings getTestSettings() {
		KalibroSettings settings = new KalibroSettings();
		settings.getServerSettings().setLoadDirectory(PROJECTS_DIRECTORY);
		settings.getServerSettings().setRemoveSources(true);
		return settings;
	}
}