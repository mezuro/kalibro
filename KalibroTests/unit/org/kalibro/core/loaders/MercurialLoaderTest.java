package org.kalibro.core.loaders;

import java.util.List;

public class MercurialLoaderTest extends RepositoryLoaderTestCase {

	@Override
	protected List<String> expectedValidationCommands() {
		return list("hg --version");
	}

	@Override
	protected List<String> expectedLoadCommands() {
		return list("hg clone " + ADDRESS + " .");
	}

	@Override
	protected List<String> expectedUpdateCommands() {
		return list("hg pull -u");
	}

	@Override
	protected String expectedMetadataDirectoryName() {
		return ".hg";
	}
}