package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Kalibro.class)
public class LoadProjectTaskTest extends KalibroTestCase {

	private LoadProjectTask loadTask;
	private Project project;
	private File loadDirectory;

	@Before
	public void setUp() {
		project = mock(Project.class);
		mockLoadDirectory();
		loadTask = new LoadProjectTask(project);
	}

	private void mockLoadDirectory() {
		loadDirectory = mock(File.class);
		KalibroSettings settings = mock(KalibroSettings.class);
		mockStatic(Kalibro.class);
		when(Kalibro.currentSettings()).thenReturn(settings);
		when(settings.getLoadDirectoryFor(project)).thenReturn(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadProject() throws IOException {
		loadTask.perform();
		Mockito.verify(project).load(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveDescription() {
		when(project.getName()).thenReturn("LoadProjectTaskTest project");
		assertEquals("loading project: LoadProjectTaskTest project", "" + loadTask);
	}
}