package org.kalibro.core.loaders;

import java.util.List;

public class MercurialLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return asList("hg --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return asList("hg clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return asList("hg pull -u");
	}
}