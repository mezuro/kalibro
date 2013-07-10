package org.kalibro.core.loaders;

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

	@Before
	@Override
	public void setUp() throws Exception {
		CommandTask commandTask = mock(CommandTask.class);
		whenNew(CommandTask.class).withArguments("git tag KalibroTag").thenReturn(commandTask);
		loader = new GitLoader();
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

	private List<String> expectedRollBackCommands() {
		return list("git checkout HEAD~1");
	}

	@Override
	protected List<String> expectedLatestCommitCommand() {
		return list("git checkout KalibroTag");
	}

	// FIXME
	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		assertDeepEquals(expectedRollBackCommands(), loader().rollBackOneCommit(true));
	}

	@Override
	@Test
	public void shouldNotRollBackWhenReachedFirstCommit() throws Exception {

	}
}