package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class MercurialLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.MERCURIAL;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("hg --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(String loadPath) {
		return Arrays.asList("hg clone " + repository.getAddress() + " " + loadPath);
	}
}