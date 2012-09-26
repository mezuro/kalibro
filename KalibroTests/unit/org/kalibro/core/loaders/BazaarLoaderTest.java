package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class BazaarLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.BAZAAR;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("bzr --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		if (update)
			return asList("bzr pull --overwrite");
		return asList("bzr branch --use-existing-dir " + repository.getAddress() + " .");
	}
}