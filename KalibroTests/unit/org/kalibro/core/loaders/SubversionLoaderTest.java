package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class SubversionLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.SUBVERSION;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("svn --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		String authentication = "--username USERNAME --password PASSWORD";
		if (update)
			return asList("svn update " + authentication);
		return asList("svn checkout " + authentication + " " + repository.getAddress() + " .");
	}
}