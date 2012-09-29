package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class LocalDirectoryLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_DIRECTORY;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("cp --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return asList("cp -ru " + repository.getAddress() + " .");
	}
}