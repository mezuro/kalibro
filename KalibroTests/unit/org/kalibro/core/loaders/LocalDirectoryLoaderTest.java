package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class LocalDirectoryLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_DIRECTORY;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("cp --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return Arrays.asList("cp -ru " + repository.getAddress() + " .");
	}
}