package org.kalibro.core.processing;

import static org.junit.Assert.*;
import static org.kalibro.core.model.ProjectFixtures.*;
import static org.kalibro.core.model.RepositoryFixtures.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Kalibro;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.Project;
import org.kalibro.core.model.enums.RepositoryType;
import org.kalibro.core.settings.KalibroSettings;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, Kalibro.class, LoadProjectTask.class})
public class LoadProjectTaskTest extends KalibroTestCase {

	private Project project;
	private LoadProjectTask loadTask;

	private String loadPath;
	private File loadDirectory;
	private CommandTask executor;

	@Before
	public void setUp() throws Exception {
		project = newHelloWorld();
		loadTask = new LoadProjectTask(project);
		mockLoadDirectory();
		mockCommandExecutor();
		PowerMockito.mockStatic(FileUtils.class);
	}

	private void mockLoadDirectory() {
		loadPath = "42";
		loadDirectory = PowerMockito.mock(File.class);
		KalibroSettings settings = PowerMockito.mock(KalibroSettings.class);
		PowerMockito.mockStatic(Kalibro.class);
		PowerMockito.when(Kalibro.currentSettings()).thenReturn(settings);
		PowerMockito.when(settings.getLoadDirectoryFor(project)).thenReturn(loadDirectory);
		PowerMockito.when(loadDirectory.getAbsolutePath()).thenReturn(loadPath);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldHaveDescription() {
		assertEquals("loading project: " + project.getName(), "" + loadTask);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldPrepareLoadDirectory() throws IOException {
		loadTask.perform();
		Mockito.verify(loadDirectory).mkdirs();
		PowerMockito.verifyStatic();
		FileUtils.cleanDirectory(loadDirectory);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void checkLoadCommandsExecution() throws Exception {
		for (RepositoryType type : RepositoryType.values())
			checkLoadCommandsExecution(type);
	}

	private void checkLoadCommandsExecution(RepositoryType repositoryType) throws Exception {
		project.setRepository(helloWorldRepository(repositoryType));
		mockCommandExecutor();
		loadTask.perform();
		verifyCommandsExecution();
	}

	private void mockCommandExecutor() throws Exception {
		executor = PowerMockito.mock(CommandTask.class);
		for (String loadCommand : project.getLoadCommands(loadPath))
			PowerMockito.whenNew(CommandTask.class).withArguments(loadCommand).thenReturn(executor);
	}

	private void verifyCommandsExecution() {
		int invocations = project.getLoadCommands(loadPath).size();
		verify(executor, times(invocations)).executeAndWait(anyLong());
	}
}