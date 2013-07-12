package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SubversionLoader.class)
public class SubversionLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("svn --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("svn checkout " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("svn update");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".svn";
	}

	@Override
	protected List<String> expectedLatestCommitCommand() {
		return list("svn update");
	}

	private List<String> expectedRollBackCommands(int revision) {
		if (revision > 1)
			return list("svn update -r " + (revision - 1));
		return null;
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		rollBackOneCommit(2 + Math.abs(new Random().nextInt()));
	}

	@Override
	@Test
	public void shouldNotRollBackWhenReachedFirstCommit() throws Exception {
		rollBackOneCommit(1);
	}

	private void rollBackOneCommit(final int revision) throws Exception {
		CommandTask commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(any(String.class)).thenReturn(commandTask);
		when(commandTask.executeAndGetOuput()).thenReturn(new InputStream() {

			@Override
			public int read() throws IOException {
				return revision;
			}
		});
		assertDeepEquals(expectedRollBackCommands(revision), loader().rollBackOneCommit(true));
	}
}
