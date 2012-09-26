package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class LocalZipLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_ZIP;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("unzip -v");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return asList("unzip -u -o -P PASSWORD " + repository.getAddress() + " -d .");
	}
}