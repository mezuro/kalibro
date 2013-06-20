package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
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
	protected List<String> expectedRollBackCommands(Long revision) {
		return Arrays.asList("svn update -r " + (revision - 1));
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		final Long revision = new Random().nextLong();
		CommandTask commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments(any(String.class)).thenReturn(commandTask);
		when(commandTask.executeAndGetOuput()).thenReturn(new InputStream() {

			@Override
			public int read() throws IOException {
				return revision.intValue();
			}
		});
		assertDeepEquals(expectedRollBackCommands(revision), loader().rollBackOneCommit(true));
	}
}