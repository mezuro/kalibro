package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class GitLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.GIT;
	}

	@Override
	public List<String> expectedValidationCommands() {
		return Arrays.asList("git --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	public List<String> expectedLoadCommands(boolean update) {
		if (update)
			return Arrays.asList("git pull origin master");
		return Arrays.asList("git clone " + repository.getAddress() + " .");
	}
}