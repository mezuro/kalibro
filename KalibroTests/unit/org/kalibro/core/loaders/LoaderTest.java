package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({FileUtils.class, Loader.class})
public class LoaderTest extends UnitTest {

	private static final String ADDRESS = "RepositoryLoaderTest address";
	private static final String LOAD_COMMAND = "RepositoryLoaderTest load command";
	private static final String UPDATE_COMMAND = "RepositoryLoaderTest update command";
	private static final String VALIDATION_COMMAND = "RepositoryLoaderTest validation command";

	private File codeDirectory;
	private CommandTask commandTask;

	private Loader loader;

	@Before
	public void setUp() throws Exception {
		codeDirectory = mock(File.class);
		mockCommandTask();
		mockStatic(FileUtils.class);
		loader = mockAbstract(Loader.class);
		doReturn(list(VALIDATION_COMMAND)).when(loader).validationCommands();
		doReturn(list(LOAD_COMMAND)).when(loader).loadCommands(ADDRESS, false);
		doReturn(list(UPDATE_COMMAND)).when(loader).loadCommands(ADDRESS, true);
	}

	private void mockCommandTask() throws Exception {
		commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(anyString()).thenReturn(commandTask);
		whenNew(CommandTask.class).withArguments(anyString(), same(codeDirectory)).thenReturn(commandTask);
	}

	@Test
	public void shouldValidate() {
		loader.validate();
	}

	@Test
	public void shouldCreateCodeDirectory() {
		loader.load(ADDRESS, codeDirectory);
		verify(codeDirectory).mkdirs();
	}

	@Test
	public void shouldDeleteCodeDirectoryIfNotUpdatable() {
		when(codeDirectory.exists()).thenReturn(true);
		doReturn(false).when(loader).isUpdatable(codeDirectory);
		loader.load(ADDRESS, codeDirectory);
		verifyStatic();
		FileUtils.deleteQuietly(codeDirectory);
	}

	@Test
	public void shouldExecuteLoadCommandsIfCodeDirectoryDoesNotExist() throws Exception {
		loader.load(ADDRESS, codeDirectory);
		verifyNew(CommandTask.class).withArguments(LOAD_COMMAND, codeDirectory);
		verify(commandTask).execute(12, HOURS);
	}

	@Test
	public void shouldExecuteUpdateCommandsIfCodeDirectoryExists() throws Exception {
		when(codeDirectory.exists()).thenReturn(true);
		doReturn(true).when(loader).isUpdatable(codeDirectory);
		loader.load(ADDRESS, codeDirectory);
		verifyNew(CommandTask.class).withArguments(UPDATE_COMMAND, codeDirectory);
		verify(commandTask).execute(12, HOURS);
	}
}