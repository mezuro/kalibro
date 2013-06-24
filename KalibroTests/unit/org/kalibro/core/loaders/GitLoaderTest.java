package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

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
		return Arrays.asList("git checkout HEAD~1");
	}

	@Override
	public void shouldRollBackOneCommitWhenIsUpdatable() throws Exception {
		assertDeepEquals(expectedRollBackCommands(), loader().rollBackOneCommit(true));
	}
}