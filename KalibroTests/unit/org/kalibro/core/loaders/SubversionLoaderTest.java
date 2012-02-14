package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class SubversionLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.SUBVERSION;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("svn --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(String loadPath) {
		String loadCommand = "svn checkout " +
			"--username " + repository.getUsername() + " --password " + repository.getPassword() + " " +
			repository.getAddress() + " " + loadPath;
		return Arrays.asList(loadCommand);
	}
}