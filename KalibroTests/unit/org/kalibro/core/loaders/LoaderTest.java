package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.kalibro.tests.UnitTest;
import org.mockito.ArgumentMatcher;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RepositoryLoader.class, FileUtils.class})
public class LoaderTest extends UnitTest {

	private static final String ADDRESS = "RepositoryLoaderTest address";
	private static final String LOAD_COMMAND = "RepositoryLoaderTest load command";
	private static final String UPDATE_COMMAND = "RepositoryLoaderTest update command";
	private static final String VALIDATION_COMMAND = "RepositoryLoaderTest validation command";
	private static final String METADATA_DIRECTORY = "RepositoryLoaderTest metadata directory";

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
		doReturn(METADATA_DIRECTORY).when(loader).metadataDirectoryName();
		mockStatic(FileUtils.class);
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

	@Test
	public void shouldCheckIfDirectoryIsUpdatable() {
		Iterator<?> iterator = mock(Iterator.class);
		Matcher<IOFileFilter> matcher = new NameFilterMatcher();
		when(FileUtils.iterateFiles(same(loadDirectory), same(FalseFileFilter.INSTANCE), argThat(matcher)))
			.thenReturn(iterator);

		when(iterator.hasNext()).thenReturn(true);
		assertTrue(loader.isUpdatable(loadDirectory));

		when(iterator.hasNext()).thenReturn(false);
		assertFalse(loader.isUpdatable(loadDirectory));
	}

	private static final class NameFilterMatcher extends ArgumentMatcher<IOFileFilter> {

		@Override
		public boolean matches(Object argument) {
			if (argument instanceof NameFileFilter) {
				String[] names = Whitebox.getInternalState(argument, String[].class);
				IOCase caseSensitivity = Whitebox.getInternalState(argument, IOCase.class);
				return Arrays.equals(names, new String[]{METADATA_DIRECTORY})
					&& caseSensitivity.equals(IOCase.SENSITIVE);
			}
			return false;
		}
	}
}