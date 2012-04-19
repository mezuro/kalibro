package org.kalibro.core.processing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.model.Project;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
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
		project = PowerMockito.mock(Project.class);
		loadTask = new LoadProjectTask(project);
		mockLoadDirectory();
		PowerMockito.mockStatic(FileUtils.class);
	}

	private void mockLoadDirectory() {
		loadDirectory = PowerMockito.mock(File.class);
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
		PowerMockito.when(settings.getLoadDirectoryFor(project)).thenReturn(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldLoadProject() throws IOException {
		loadTask.perform();
		Mockito.verify(project).load(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveDescription() {
		assertEquals("loading project: " + project.getName(), "" + loadTask);
	}
}