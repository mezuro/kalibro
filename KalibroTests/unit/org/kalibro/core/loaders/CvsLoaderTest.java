package org.kalibro.core.loaders;

import java.util.List;

public class CvsLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("cvs --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("cvs -z3 -d " + ADDRESS + " checkout -d . -P .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("cvs update");
	}
}