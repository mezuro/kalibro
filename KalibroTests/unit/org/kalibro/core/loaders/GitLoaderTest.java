package org.kalibro.core.loaders;

import java.io.InputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kalibro.core.command.CommandTask;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(GitLoader.class)
public class GitLoaderTest extends RepositoryLoaderTestCase {

	private static final String MY_BRANCH = "branch";
	private InputStream outputCommand;
	private CommandTask commandTask;

	@Before
	@Override
	public void setUp() throws Exception {
		commandTask = mock(CommandTask.class);
		outputCommand = mock(InputStream.class);
		whenNew(CommandTask.class).withArguments("git branch | grep \\*").thenReturn(commandTask);
		whenNew(CommandTask.class).withArguments("git checkout HEAD~1").thenReturn(commandTask);
		when(commandTask.executeAndGetOuput()).thenReturn(outputCommand);
		when(outputCommand.toString()).thenReturn(MY_BRANCH);
		loader = new GitLoader();
		verify(outputCommand, once()).skip(2);
	}

	@Override
	public List<String> expectedValidationCommands() {
		return list("git --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("git clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("git pull origin master");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".git";
	}

	private List<String> expectedRollBackCommands(String gitMessage) {
		if (gitMessage.contains("error: pathspec 'HEAD~1' did not match any file(s) known to git."))
			return null;
		return list("git checkout HEAD~1");
	}

	@Override
	protected List<String> expectedLatestCommitCommand() {
		return list("git checkout " + MY_BRANCH);
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		String gitMessage = "A few git text...";
		when(outputCommand.toString()).thenReturn(gitMessage);
		CommandTask revertCommandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments("git checkout HEAD@{1}").thenReturn(revertCommandTask);
		assertDeepEquals(expectedRollBackCommands(gitMessage), loader().rollBackOneCommit(true));
		verify(revertCommandTask, once()).execute();
	}

	@Override
	@Test
	public void shouldNotRollBackWhenReachedFirstCommit() throws Exception {
		String gitErrorMessage = "error: pathspec 'HEAD~1' did not match any file(s) known to git.";
		when(outputCommand.toString()).thenReturn(gitErrorMessage);
		assertDeepEquals(expectedRollBackCommands(gitErrorMessage), loader().rollBackOneCommit(true));
	}
}