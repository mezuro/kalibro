package org.kalibro.core.loaders;

import java.util.List;

public class GitLoaderTest extends RepositoryLoaderTestCase {

	@Override
	public List<String> expectedValidationCommands() {
		return asList("git --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("git clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("git pull origin master");
	}
}