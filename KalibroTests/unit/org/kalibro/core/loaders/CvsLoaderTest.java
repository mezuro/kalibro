package org.kalibro.core.loaders;

import java.util.List;

public class CvsLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("cvs --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("cvs -z3 -d " + ADDRESS + " checkout -d . -P .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("cvs update");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return "CVSROOT";
	}
}