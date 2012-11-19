package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.HOURS;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryLoader.class)
public class RepositoryLoaderTest extends UnitTest {

	private static final String ADDRESS = "RepositoryLoaderTest address";
	private static final String LOAD_COMMAND = "RepositoryLoaderTest load command";
	private static final String UPDATE_COMMAND = "RepositoryLoaderTest update command";
	private static final String VALIDATION_COMMAND = "RepositoryLoaderTest validation command";

	private File loadDirectory;
	private CommandTask commandTask;

	private RepositoryLoader loader;

	@Before
	public void setUp() throws Exception {
		loadDirectory = mock(File.class);
		mockCommandTask();
		loader = mockAbstract(RepositoryLoader.class);
		doReturn(list(VALIDATION_COMMAND)).when(loader).validationCommands();
		doReturn(list(LOAD_COMMAND)).when(loader).loadCommands(ADDRESS, false);
		doReturn(list(UPDATE_COMMAND)).when(loader).loadCommands(ADDRESS, true);
	}

	private void mockCommandTask() throws Exception {
		commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(anyString()).thenReturn(commandTask);
		whenNew(CommandTask.class).withArguments(anyString(), same(loadDirectory)).thenReturn(commandTask);
	}

	@Test
	public void shouldValidate() {
		loader.validate();
	}

	@Test
	public void shouldCreateLoadDirectory() {
		loader.load(ADDRESS, loadDirectory);
		verify(loadDirectory).mkdirs();
	}

	@Test
	public void shouldExecuteLoadCommandsOnFirstLoad() throws Exception {
		loader.load(ADDRESS, loadDirectory);
		verifyNew(CommandTask.class).withArguments(LOAD_COMMAND, loadDirectory);
		verify(commandTask).execute(10, HOURS);
	}

	@Test
	public void shouldExecuteUpdateCommandsIfLoadDirectoryExists() throws Exception {
		when(loadDirectory.exists()).thenReturn(true);
		loader.load(ADDRESS, loadDirectory);
		verifyNew(CommandTask.class).withArguments(UPDATE_COMMAND, loadDirectory);
		verify(commandTask).execute(10, HOURS);
	}
}