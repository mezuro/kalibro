package org.kalibro.core.loaders;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class GitLoaderTest extends RepositoryLoaderTestCase {

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
		return list("git checkout");
	}

	@Override
	@Test
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		assertDeepEquals(expectedRollBackCommands(), loader().rollBackOneCommit(true));
	}
}