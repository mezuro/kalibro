package org.kalibro.core.loaders;

import java.util.Arrays;
import java.util.List;

import org.kalibro.core.model.enums.RepositoryType;

public class LocalZipLoaderTest extends ProjectLoaderTestCase {

	@Override
	protected RepositoryType getRepositoryType() {
		return RepositoryType.LOCAL_ZIP;
	}

	@Override
	protected List<String> expectedValidationCommands() {
		return Arrays.asList("unzip -v");
	}

	@Override
	protected boolean shouldSupportAuthentication() {
		return true;
	}

	@Override
	protected List<String> expectedLoadCommands(boolean update) {
		return Arrays.asList("unzip -u -o -P PASSWORD " + repository.getAddress() + " -d .");
	}
}