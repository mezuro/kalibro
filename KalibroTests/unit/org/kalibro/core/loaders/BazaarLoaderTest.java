package org.kalibro.core.loaders;

import java.util.List;

public class BazaarLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("bzr --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("bzr branch --use-existing-dir " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("bzr pull --overwrite");
	}
}