package org.kalibro.core.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(MercurialLoader.class)
public class MercurialLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("hg --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("hg clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("hg pull -u");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".hg";
	}

	private List<String> expectedRollBackCommands(int revision) {
		return Arrays.asList("hg update " + (revision - 1));
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		final int revision = Math.abs(new Random().nextInt());
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