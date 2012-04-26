package org.kalibro.core.loaders;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.powermock.api.mockito.PowerMockito.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.KalibroException;
import org.kalibro.KalibroTestCase;
import org.kalibro.core.command.CommandTask;
import org.kalibro.core.model.Repository;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ProjectLoader.class)
public class ProjectLoaderTest extends KalibroTestCase {

	private static final String LOAD_COMMAND = "ProjectLoaderTest load command";
	private static final String UPDATE_COMMAND = "ProjectLoaderTest update command";
	private static final String VALIDATION_COMMAND = "ProjectLoaderTest validation command";

	private File loadDirectory;
	private Repository repository;
	private CommandTask commandTask;

	private ProjectLoader loader;

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

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldValidate() {
		assertTrue(loader.validate());

		doThrow(new KalibroException("")).when(commandTask).executeAndWait(ProjectLoader.VALIDATION_TIMEOUT);
		assertFalse(loader.validate());
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldCreateLoadDirectoryIfNotExistentOnLoad() {
		loader.load(repository, loadDirectory);
		Mockito.verify(loadDirectory).mkdirs();
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteLoadCommandsOnFirstLoad() throws Exception {
		loader.load(repository, loadDirectory);
		verifyNew(CommandTask.class).withArguments(LOAD_COMMAND, loadDirectory);
		Mockito.verify(commandTask).executeAndWait(ProjectLoader.LOAD_TIMEOUT);
	}

	@Test(timeout = UNIT_TIMEOUT)
	public void shouldExecuteUpdateCommandsIfLoadDirectoryExists() throws Exception {
		when(loadDirectory.exists()).thenReturn(true);
		loader.load(repository, loadDirectory);
		verifyNew(CommandTask.class).withArguments(UPDATE_COMMAND, loadDirectory);
		Mockito.verify(commandTask).executeAndWait(ProjectLoader.LOAD_TIMEOUT);
	}

	private class FakeLoader extends ProjectLoader {

		@Override
		public boolean supportsAuthentication() {
			return false;
		}

		@Override
		protected List<String> getValidationCommands() {
			return Arrays.asList(VALIDATION_COMMAND);
		}

		@Override
		protected List<String> getLoadCommands(Repository repo, boolean update) {
			return Arrays.asList(update ? UPDATE_COMMAND : LOAD_COMMAND);
		}
	}
}