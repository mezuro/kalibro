package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.ProjectState;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class LoadSourceTaskTest extends KalibroTestCase {

	private Project project;
	private File loadDirectory;

	private LoadSourceTask loadTask;

	@Before
	public void setUp() {
		project = mock(Project.class);
		mockLoadDirectory();
		loadTask = new LoadSourceTask(project);
	}

	private void mockLoadDirectory() {
		loadDirectory = mock(File.class);
		KalibroSettings settings = mock(KalibroSettings.class);
		mockStatic(Kalibro.class);
		when(Kalibro.currentSettings()).thenReturn(settings);
		when(settings.getLoadDirectoryFor(project)).thenReturn(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkTaskState() {
		assertEquals(ProjectState.LOADING, loadTask.getTaskState());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadProject() {
		loadTask.performAndGetResult();
		Mockito.verify(project).load(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldReturnProjectResult() {
		assertSame(loadTask.projectResult, loadTask.performAndGetResult());
	}
}