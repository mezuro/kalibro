package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.RepositoryType;

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
	protected List<String> expectedLoadCommands(boolean update) {
		if (update)
			return Arrays.asList("hg pull -u");
		return Arrays.asList("hg clone " + repository.getAddress() + " .");
	}
}