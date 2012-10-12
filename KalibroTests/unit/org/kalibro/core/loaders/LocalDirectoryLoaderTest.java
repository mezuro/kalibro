package org.kalibro.core.loaders;

import java.util.List;

public class LocalDirectoryLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("cp --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("cp -ru " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("cp -ru " + ADDRESS + " .");
	}
}