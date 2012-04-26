package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class LocalTarballLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_TARBALL;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("tar --version");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return false;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return Arrays.asList("tar -x --keep-newer-files -f " + repository.getAddress() + " -C .");
	}
}