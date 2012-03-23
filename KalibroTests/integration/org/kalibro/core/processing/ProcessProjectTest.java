package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.FileProcessStreamLogger;
import org.kalibro.core.model.ModuleResult;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.ProjectResult;
import org.kalibro.core.persistence.dao.ModuleResultDao;
import org.kalibro.core.persistence.dao.ProjectDao;
import org.kalibro.core.persistence.dao.ProjectResultDao;
import org.kalibro.core.settings.KalibroSettings;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.api.support.membermodification.MemberModifier;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

// TODO test whole project processing
@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({FileProcessStreamLogger.class, Kalibro.class})
public class ProcessProjectTest extends KalibroTestCase {

	private Project project;

	private ProjectDao projectDao;
	private ModuleResultDao moduleResultDao;
	private ProjectResultDao projectResultDao;

	private ProcessProjectTask processTask;

	@Before
	public void setUp() {
		project = helloWorld();
		mockKalibro();
		MemberModifier.suppress(FileProcessStreamLogger.class.getMethods());
		processTask = new ProcessProjectTask(project.getName());
	}

	private void mockKalibro() {
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.when(settings.getLoadDirectoryFor(project)).thenReturn(HELLO_WORLD_DIRECTORY);
		projectDao = PowerMockito.mock(ProjectDao.class);
		moduleResultDao = PowerMockito.mock(ModuleResultDao.class);
		projectResultDao = PowerMockito.mock(ProjectResultDao.class);
		PowerMockito.when(projectDao.getProject(project.getName())).thenReturn(project);

		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
		PowerMockito.when(Kalibro.getProjectDao()).thenReturn(projectDao);
		PowerMockito.when(Kalibro.getModuleResultDao()).thenReturn(moduleResultDao);
		PowerMockito.when(Kalibro.getProjectResultDao()).thenReturn(projectResultDao);
	}

	@After
	public void tearDown() {
		FileUtils.deleteQuietly(HELLO_WORLD_DIRECTORY);
	}

	@Test(timeout = INTEGRATION_TIMEOUT)
	public void testProcess() {
		processTask.perform();
		assertLoaded();
		assertAnalyzed();
	}

	private void assertLoaded() {
		assertTrue(HELLO_WORLD_DIRECTORY.exists());
		Iterator<?> files = FileUtils.iterateFiles(HELLO_WORLD_DIRECTORY, null, true);
		assertEquals("HelloWorld.c", ((File) files.next()).getName());
		assertFalse(files.hasNext());
	}

	private void assertAnalyzed() {
		verify(projectDao).save(project);
		verify(projectResultDao).save(any(ProjectResult.class));
		verify(moduleResultDao).save(any(ModuleResult.class), anyString());
	}
}