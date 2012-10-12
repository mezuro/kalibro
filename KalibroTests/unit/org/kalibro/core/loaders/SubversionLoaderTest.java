package org.kalibro.core.loaders;

import java.util.List;

public class SubversionLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("svn --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("svn checkout " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("svn update");
	}
}