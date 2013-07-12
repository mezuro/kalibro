package org.kalibro.core.loaders;

import java.util.List;

public class GitLoaderTest extends RepositoryLoaderTestCase {

	@Override
	public List<String> expectedValidationCommands() {
		return list("git --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("git clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("git pull");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".git";
	}
}