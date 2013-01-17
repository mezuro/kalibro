package org.kalibro.core.loaders;

import java.util.List;

public class SubversionLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("svn --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("svn checkout " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("svn update");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".svn";
	}
}