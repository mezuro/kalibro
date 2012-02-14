package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class BazaarLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.BAZAAR;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("bzr --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(String loadPath) {
		return Arrays.asList("bzr branch --use-existing-dir " + repository.getAddress() + " " + loadPath);
	}
}