package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class CvsLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.CVS;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("cvs --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(String loadPath) {
		return Arrays.asList("cvs -z3 -d " + repository.getAddress() + " checkout -d " + loadPath + " -P .");
	}
}