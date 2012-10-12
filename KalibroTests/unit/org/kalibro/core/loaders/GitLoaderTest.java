package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class GitLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.GIT;
	}

	@Override
	public List<String> expectedValidationCommands() {
		return asList("git --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	public List<String> expectedLoadCommands(boolean update) {
		if (update)
			return asList("git pull origin master");
		return asList("git clone " + repository.getAddress() + " .");
	}
}