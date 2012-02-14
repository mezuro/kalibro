package org.kalibro.core.processing;

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
import org.kalibro.core.command.CommandExecutor;
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
	private CommandExecutor executor;

	@Before
	public void setUp() throws Exception {
		project = helloWorld();
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
		executor = PowerMockito.mock(CommandExecutor.class);
		for (String loadCommand : project.getLoadCommands(loadPath))
			PowerMockito.whenNew(CommandExecutor.class).withArguments(loadCommand).thenReturn(executor);
	}

	private void verifyCommandsExecution() {
		int invocations = project.getLoadCommands(loadPath).size();
		verify(executor, times(invocations)).executeCommandWithTimeout(anyLong());
	}
}