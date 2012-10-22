package org.kalibro.core.loaders;

import java.util.List;

public class BazaarLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("bzr --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("bzr branch --use-existing-dir " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("bzr pull --overwrite");
	}
}