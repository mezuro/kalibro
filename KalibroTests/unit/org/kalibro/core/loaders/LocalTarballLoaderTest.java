package org.kalibro.core.loaders;

import java.util.List;

import org.kalibro.RepositoryType;

public class LocalTarballLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_TARBALL;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("tar --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return asList("tar -x --keep-newer-files -f " + repository.getAddress() + " -C .");
	}
}