package org.kalibro.core.loaders;

import java.util.List;

public class LocalZipLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("unzip -v");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("unzip -u -o " + ADDRESS + " -d .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("unzip -u -o " + ADDRESS + " -d .");
	}
}