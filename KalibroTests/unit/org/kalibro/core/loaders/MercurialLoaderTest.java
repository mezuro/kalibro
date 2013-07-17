package org.kalibro.core.loaders;

import java.io.InputStream;
import java.util.List;
import java.util.Random;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MercurialLoader.class, IOUtils.class})
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

	private List<String> expectedRollBackCommands(String revision) {
		Long rev = new Long(revision);
		if (rev > 1)
			return list("hg update " + (rev - 1));
		return null;
	}

	@Override
	protected List<String> expectedLatestCommitCommand() {
		return list("hg update");
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		Long revision = 2 + Math.abs(new Random().nextLong());
		rollBackOneCommit(revision.toString());
	}

	@Override
	@Test
	public void shouldNotRollBackWhenReachedFirstCommit() throws Exception {
		rollBackOneCommit("1");
	}

	private void rollBackOneCommit(String revision) throws Exception {
		mockStatic(IOUtils.class);
		CommandTask commandTask = mock(CommandTask.class);
		InputStream commandOutput = mock(InputStream.class);
		whenNew(CommandTask.class).withArguments(any(String.class)).thenReturn(commandTask);
		when(commandTask.executeAndGetOuput()).thenReturn(commandOutput);
		when(IOUtils.toString(commandOutput)).thenReturn(revision);
		assertDeepEquals(expectedRollBackCommands(revision), loader().rollBackOneCommit(true));
	}
}