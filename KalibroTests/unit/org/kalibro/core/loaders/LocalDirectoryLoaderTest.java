package org.kalibro.core.loaders;

import java.util.List;

public class LocalDirectoryLoaderTest extends LoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("cp --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("cp -ru " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("cp -ru " + ADDRESS + " .");
	}
}