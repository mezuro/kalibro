package org.kalibro.core.loaders;

import static java.util.concurrent.TimeUnit.*;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
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
@PrepareForTest({FileUtils.class, RepositoryLoader.class})
public class RepositoryLoaderTest extends UnitTest {

	private static final String METADATA_DIRECTORY = "RepositoryLoaderTest metadata directory";

	private File loadDirectory;
	private RepositoryLoader loader;
	private CommandTask commandTask;

	@Before
	public void setUp() throws Exception {
		loadDirectory = mock(File.class);
		loader = mockAbstract(RepositoryLoader.class);
		commandTask = mock(CommandTask.class);
		mockStatic(FileUtils.class);
		doReturn(METADATA_DIRECTORY).when(loader).metadataDirectoryName();
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

	@Test
	public void shouldNotLoadForHistoricProcessing() throws Exception {
		doReturn(true).when(loader).isUpdatable(any(File.class));
		doReturn(null).when(loader).rollBackOneCommit(true);
		whenNew(CommandTask.class).withArguments(null, loadDirectory).thenReturn(commandTask);
		assertFalse(loader.loadForHistoricProcessing(loadDirectory));
		verifyNew(CommandTask.class, never());
	}

	@Test
	public void shouldLoadForHistoricProcessing() throws Exception {
		ArrayList<String> commands = new ArrayList<String>();
		commands.add("first command");
		commands.add("second command");
		doReturn(true).when(loader).isUpdatable(any(File.class));
		doReturn(commands).when(loader).rollBackOneCommit(true);
		for (String command : commands) {
			whenNew(CommandTask.class).withArguments(command, loadDirectory).thenReturn(commandTask);
		}
		assertTrue(loader.loadForHistoricProcessing(loadDirectory));
		verify(commandTask, times(2)).execute(12, HOURS);
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
