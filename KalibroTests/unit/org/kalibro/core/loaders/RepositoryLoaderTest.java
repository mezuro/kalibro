package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.Repository;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.UnitTest;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(RepositoryLoader.class)
public class RepositoryLoaderTest extends UnitTest {

	private static final String LOAD_COMMAND = "RepositoryLoaderTest load command";
	private static final String UPDATE_COMMAND = "RepositoryLoaderTest update command";
	private static final String VALIDATION_COMMAND = "RepositoryLoaderTest validation command";

	private File loadDirectory;
	private Repository repository;
	private CommandTask commandTask;

	private RepositoryLoader loader;

	@Before
	public void setUp() throws Exception {
		loadDirectory = mock(File.class);
		repository = mock(Repository.class);
		mockCommandTask();
		loader = new FakeLoader();
	}

	private void mockCommandTask() throws Exception {
		commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(anyString()).thenReturn(commandTask);
		whenNew(CommandTask.class).withArguments(anyString(), same(loadDirectory)).thenReturn(commandTask);
	}

	@Test
	public void shouldValidate() {
		assertTrue(loader.validate());

		doThrow(mock(RuntimeException.class)).when(commandTask).execute(30, SECONDS);
		assertFalse(loader.validate());
	}

	@Test
	public void shouldCreateLoadDirectory() {
		loader.load(repository, loadDirectory);
		verify(loadDirectory).mkdirs();
	}

	@Test
	public void shouldExecuteLoadCommandsOnFirstLoad() throws Exception {
		loader.load(repository, loadDirectory);
		verifyNew(CommandTask.class).withArguments(LOAD_COMMAND, loadDirectory);
		verify(commandTask).execute(10, HOURS);
	}

	@Test
	public void shouldExecuteUpdateCommandsIfLoadDirectoryExists() throws Exception {
		when(loadDirectory.exists()).thenReturn(true);
		loader.load(repository, loadDirectory);
		verifyNew(CommandTask.class).withArguments(UPDATE_COMMAND, loadDirectory);
		verify(commandTask).execute(10, HOURS);
	}

	private class FakeLoader extends RepositoryLoader {

		@Override
		protected List<String> validationCommands() {
			return asList(VALIDATION_COMMAND);
		}

		@Override
		protected List<String> loadCommands(Repository repo, boolean update) {
			return asList(update ? UPDATE_COMMAND : LOAD_COMMAND);
		}
	}
}